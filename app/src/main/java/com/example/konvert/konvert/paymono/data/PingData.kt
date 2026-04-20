package com.example.konvert.konvert.paymono.data

import kotlinx.serialization.Serializable

@Serializable
data class PingData(
    val id: String,
    val user_id: String
)
