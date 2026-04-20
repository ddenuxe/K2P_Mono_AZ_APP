package com.example.konvert.konvert.paymono.data

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable


@Immutable
@Serializable
data class JarAccount(
    val id: String,
    val sendId: String,
    val balance: Int,
    val creditLimit: Int? = null,
    val type: String? = null,
    val currencyCode: Int,
    val cashbackType: String? = null,
    val maskedPan: List<String>? = null,
    val iban: String? = null,
    val goal: Int? = null,
    val description: String? = null,
    val title: String? = null,
    val card: String = ""
)
