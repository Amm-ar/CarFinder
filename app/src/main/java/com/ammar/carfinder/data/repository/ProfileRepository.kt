package com.ammar.carfinder.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.ammar.carfinder.data.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.ByteArrayOutputStream

class ProfileRepository {

    val auth = SupabaseClient.client.auth
    private val storage = SupabaseClient.client.storage

    suspend fun updateProfile(fullName: String, phone: String) {
        auth.updateUser {
            data = buildJsonObject {
                put("full_name", fullName)
                put("phone", phone)
            }
        }
        auth.refreshCurrentSession()
    }

    suspend fun uploadProfileImage(context: Context, imageUri: Uri): String {
        val userId = auth.currentUserOrNull()?.id ?: error("User not logged in")
        val compressedImage = compressImage(context, imageUri)

        val filePath = "avatars/${userId}.jpg"
        val bucket = storage.from("avatars")
        
        // Upload the compressed image using the correct options DSL
        bucket.upload(filePath, compressedImage) {
            upsert = true
        }

        // Get the public URL and add a cache buster
        val publicUrl = bucket.publicUrl(filePath)
        val cacheBustedUrl = "$publicUrl?t=${System.currentTimeMillis()}"


        // Update the user's metadata with the new avatar URL
        auth.updateUser {
            data = buildJsonObject {
                put("avatar_url", cacheBustedUrl)
            }
        }

        // Force a session refresh to get the latest user metadata
        auth.refreshCurrentSession()

        return cacheBustedUrl
    }

    private fun compressImage(context: Context, uri: Uri, maxWidth: Int = 500, maxHeight: Int = 500): ByteArray {
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream) // 80% quality
        return outputStream.toByteArray()
    }
}
