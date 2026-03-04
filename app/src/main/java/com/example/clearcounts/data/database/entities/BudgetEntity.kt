package com.example.clearcounts.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "tipo") val tipo: String,
    @ColumnInfo(name = "cantidad_requerida") val cantidadRequerida: Double,
    @ColumnInfo(name = "cantidad_acumulada") val cantidadAcumulada: Double,
    @ColumnInfo(name = "prestador") val prestador: String?,
    @ColumnInfo(name = "fecha_inicio") val fechaInicio: String?,
    @ColumnInfo(name = "fecha_limite") val fechaLimite: String?,
    @ColumnInfo(name = "nota") val nota: String?
)

