package com.example.konvert.konvert.paymono.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.konvert.konvert.paymono.data.MyTransaction
import com.example.konvert.konvert.paymono.data.PostData
import com.example.konvert.konvert.paymono.data.UpTrans

@Database(
    entities = [UpTrans::class],
    version = 1
)
abstract class TransactionDatabase: RoomDatabase() {

    abstract val dao: TransactionDAO

    companion object {

        private var INSTANCE: TransactionDatabase? = null

        fun getInstance(context: Context): TransactionDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, TransactionDatabase::class.java, "trans.db").build()
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}