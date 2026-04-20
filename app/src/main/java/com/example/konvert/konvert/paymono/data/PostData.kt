package com.example.konvert.konvert.paymono.data

import kotlinx.serialization.Serializable

@Serializable
data class PostData(
    val card: String,
    val iban: String,
    val type: String,
    val link: String,
    val bank: String,
    val transaction_id: String,
    val time: String,
    val date: String,
    val description: String,
    val mcc: Int,
    val originalMcc: Int,
    val amount: Int,
    val operationAmount: Int,
    val currencyCode: Int,
    val commissionRate: Int,
    val cashbackAmount: Int,
    val balance: Int,
    val hold: Boolean,
    val app: String,
    val id: String,
    val user_id: String

)
