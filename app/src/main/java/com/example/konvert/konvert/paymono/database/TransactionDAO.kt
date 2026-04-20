package com.example.konvert.konvert.paymono.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.konvert.konvert.paymono.data.MyTransaction
import com.example.konvert.konvert.paymono.data.UpTrans
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {

    @Upsert
    suspend fun upsertTransaction(UpTrans: UpTrans)

    @Query("DELETE FROM uptrans")
    suspend fun deleteAll()

    @Query("SELECT EXISTS (SELECT 1 FROM uptrans WHERE id = :id)")
    fun getTransactionById(id: String): Boolean

    @Query("SELECT * FROM uptrans")
    fun getAll(): List<UpTrans>
}