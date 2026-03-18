package com.example.clearcounts.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "mensaje") val mensaje: String,
    @ColumnInfo(name = "fecha") val fecha: String, // formato "dd-MM-yyyy"
    @ColumnInfo(name = "hora") val hora: String,  // formato "hh:mm a"
    @ColumnInfo(name = "leida") val leida: Boolean = false
)