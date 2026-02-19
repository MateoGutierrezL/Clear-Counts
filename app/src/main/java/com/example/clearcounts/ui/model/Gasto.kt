package com.example.clearcounts.ui.model

data class Gasto(
    val id: Int,
    val cantidad: String,
    val categoria: String,
    val nota: String?,
    val fecha: String,
    val hora: String
)
