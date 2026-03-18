package com.example.clearcounts.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payment_method")
data class PaymentMethodEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: String = "",
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "icono") val icono: String,
    @ColumnInfo(name = "es_default") val esDefault: Boolean = false
)