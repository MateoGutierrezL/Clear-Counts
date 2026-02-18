package com.example.clearcounts.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gasto")
data class ExpenseEntity (

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "categoria") val categoria: String,
    @ColumnInfo(name = "cantidad") val cantidad: Double,
    @ColumnInfo(name = "hora")val hora: String,
    @ColumnInfo(name = "fecha") val fecha: String,
    @ColumnInfo(name = "nota")val nota: String

)