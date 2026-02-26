package com.example.clearcounts.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "icono") val icono: String,       // Nombre del recurso drawable, ej: "ic_food"
    @ColumnInfo(name = "tipo") val tipo: String        // "INCOME" o "EXPENSE"
)