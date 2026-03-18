package com.example.clearcounts.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "usuario")
data class UserEntity (

    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "nombre") val nombre: String,
    @ColumnInfo(name = "numero")val numero: String,
    @ColumnInfo(name = "correo") val correo: String,
    @ColumnInfo(name = "contrasena")val contrasena: String,
    @ColumnInfo(name = "avatar") val avatar: String = "perro"

)