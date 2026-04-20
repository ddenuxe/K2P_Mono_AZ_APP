package com.example.konvert.konvert.paymono.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class MyTransaction(
    @PrimaryKey
    val id: String,
    val time: Long? = null,
    val description: String? = null,
    val mcc: Int? = null,
    val originalMcc: Int? = null,
    val amount: Int? = null,
    val operationAmount: Int? = null,
    val currencyCode: Int? = null,
    val commissionRate: Int? = null,
    val cashbackAmount: Int? = null,
    val balance: Int? = null,
    val hold: Boolean? = null,
    val comment: String? = null,
    val receiptId: String? = null,
    val invoiceId: String? = null,
    val counterEdrpou: String? = null,
    val counterIban: String? = null,
    val counterName: String? = null

)
