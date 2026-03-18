package com.example.clearcounts.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recurrente")
data class RecurringEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: String = "",
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "categoria") val categoria: String,
    @ColumnInfo(name = "cantidad") val cantidad: Double,
    @ColumnInfo(name = "tipo") val tipo: String,
    @ColumnInfo(name = "dia_del_mes") val diaDelMes: Int,
    @ColumnInfo(name = "activo") val activo: Boolean = true,
    @ColumnInfo(name = "metodo_pago") val metodoPago: String = "Efectivo",
    @ColumnInfo(name = "ultima_ejecucion") val ultimaEjecucion: String? = null
)