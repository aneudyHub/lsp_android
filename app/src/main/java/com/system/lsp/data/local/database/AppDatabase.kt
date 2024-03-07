package com.system.lsp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.system.lsp.data.local.database.AppDatabase.Companion.DATABASE_VERSION
import com.system.lsp.data.local.database.dao.CustomersDao
import com.system.lsp.data.local.database.dao.LoanDetailsDao
import com.system.lsp.data.local.database.dao.LoansDao
import com.system.lsp.data.local.database.dao.PaymentDetailDao
import com.system.lsp.data.local.database.dao.PaymentsDao
import com.system.lsp.data.local.database.entities.Converters
import com.system.lsp.data.local.database.entities.CustomerEntity
import com.system.lsp.data.local.database.entities.LoanEntity
import com.system.lsp.data.local.database.entities.LoansDetailsEntity
import com.system.lsp.data.local.database.entities.PaymentDetailsEntity
import com.system.lsp.data.local.database.entities.PaymentEntity

@Database(
    version = DATABASE_VERSION,
    exportSchema = false,
    entities = [
        CustomerEntity::class,
        LoanEntity::class,
        LoansDetailsEntity::class,
        PaymentEntity::class,
        PaymentDetailsEntity::class
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun customersDao(): CustomersDao
    abstract fun loansDao(): LoansDao
    abstract fun loansDetailsDao(): LoanDetailsDao
    abstract fun paymentsDao(): PaymentsDao
    abstract fun paymentsDetailsDao(): PaymentDetailDao


    companion object {
        const val DATABASE_NAME = "lsp_local_db"
        const val DATABASE_VERSION = 1


        const val CUSTOMERS_TABLE_NAME = "customers"
        const val LOANS_TABLE_NAME = "loans"
        const val LOANS_DETAILS_TABLE_NAME = "loans_details"
        const val PAYMENTS_TABLE_NAME = "payments"
        const val PAYMENTS_DETAILS_TABLE_NAME = "payment_details"
    }
}