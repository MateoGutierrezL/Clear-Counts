package com.example.clearcounts.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "user_id") val userId: String = "",
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "icono") val icono: String,
    @ColumnInfo(name = "tipo") val tipo: String
)