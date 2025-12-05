package com.ammar.carfinder.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class CarStatus {
    @SerialName("LOST") LOST,
    @SerialName("FOUND") FOUND
}

@Serializable
data class Car(
    val id: Long = 0,
    @SerialName("created_at")
    val createdAt: String = "", // Will be overwritten before insert
    val make: String,
    val model: String,
    val year: Int? = null,
    val color: String? = null,
    @SerialName("license_plate")
    val licensePlate: String? = null,
    @SerialName("chassis_number")
    val chassisNumber: String? = null,
    val status: CarStatus,
    val description: String? = null,
    @SerialName("contact_info")
    val contactInfo: String? = null,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("user_id")
    val userId: String? = null
)
