package com.example.konvert.konvert.paymono.data

data class Card(
    val cardColor: String,
    val cardNumber: String,
    val iban: String,
    val link: String? = null
)
