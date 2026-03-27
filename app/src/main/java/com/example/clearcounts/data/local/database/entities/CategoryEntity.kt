package com.example.clearcounts.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val firestoreId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id") val userId: String = "",
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "icono") val icono: String,
    @ColumnInfo(name = "tipo") val tipo: String
)