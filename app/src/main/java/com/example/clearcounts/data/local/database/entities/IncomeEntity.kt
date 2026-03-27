package com.example.clearcounts.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clearcounts.ui.screens.Categorias.Categorias
import java.util.UUID

@Entity(tableName = "ingreso")
data class IncomeEntity (

    @PrimaryKey val firestoreId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id") val userId: String = "",
    @ColumnInfo(name = "categoria") val categoria: String,
    @ColumnInfo(name = "cantidad") val cantidad: Double,
    @ColumnInfo(name = "hora")val hora: String,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "nota")val nota: String?,
    @ColumnInfo(name = "metodo_pago") val metodoPago: String = "Efectivo"

)