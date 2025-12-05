package com.ammar.carfinder.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.ammar.carfinder.data.SupabaseClient
import com.ammar.carfinder.data.model.Car
import com.ammar.carfinder.data.model.CarStatus
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.util.UUID

class CarRepository {

    private val supabase = SupabaseClient.client

    suspend fun getAllCars(): List<Car> {
        return supabase.from("cars").select().decodeList<Car>()
    }

    suspend fun getLostCars(): List<Car> {
        return supabase.from("cars")
            .select {
                filter {
                    eq("status", CarStatus.LOST)
                }
            }.decodeList<Car>()
    }

    suspend fun getFoundCars(): List<Car> {
        return supabase.from("cars")
            .select {
                filter {
                    eq("status", CarStatus.FOUND)
                }
            }.decodeList<Car>()
    }

    suspend fun searchCars(query: String): List<Car> {
        // Searches across make, model, license plate, color, or chassis number
        return supabase.from("cars")
            .select {
                filter {
                    or {
                        ilike("make", "%$query%")
                        ilike("model", "%$query%")
                        ilike("license_plate", "%$query%")
                        ilike("color", "%$query%")
                        ilike("chassis_number", "%$query%")
                    }
                }
            }.decodeList<Car>()
    }

    suspend fun uploadCarImage(context: Context, imageUri: Uri): String {
        val compressedImage = compressImage(context, imageUri)
        val fileName = "${UUID.randomUUID()}.jpg"
        val bucket = supabase.storage.from("car-images")
        
        bucket.upload(fileName, compressedImage) {
            upsert = false
        }
        
        return bucket.publicUrl(fileName)
    }

    private fun compressImage(context: Context, uri: Uri, maxWidth: Int = 1024, maxHeight: Int = 1024): ByteArray {
        val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        context.contentResolver.openInputStream(uri).use { 
            BitmapFactory.decodeStream(it, null, options) 
        }

        var inSampleSize = 1
        if (options.outHeight > maxHeight || options.outWidth > maxWidth) {
            val halfHeight = options.outHeight / 2
            val halfWidth = options.outWidth / 2
            while ((halfHeight / inSampleSize) >= maxHeight && (halfWidth / inSampleSize) >= maxWidth) {
                inSampleSize *= 2
            }
        }
        
        val finalOptions = BitmapFactory.Options().apply { this.inSampleSize = inSampleSize }
        val bitmap = context.contentResolver.openInputStream(uri).use { 
            BitmapFactory.decodeStream(it, null, finalOptions) 
        }!!
        
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return outputStream.toByteArray()
    }

    suspend fun addCar(car: Car) {
        val userId = supabase.auth.currentUserOrNull()?.id ?: error("User not authenticated")
        
        // Create a JSON object manually to exclude 'id' and let the DB handle it
        val carJson = buildJsonObject {
            put("make", car.make)
            put("model", car.model)
            put("year", car.year)
            put("color", car.color)
            put("license_plate", car.licensePlate)
            put("chassis_number", car.chassisNumber)
            put("status", car.status.name) // Using enum name
            put("description", car.description)
            put("contact_info", car.contactInfo)
            put("image_url", car.imageUrl)
            put("user_id", userId)
            put("created_at", Instant.now().toString())
        }
        
        supabase.from("cars").insert(carJson)
    }
}
