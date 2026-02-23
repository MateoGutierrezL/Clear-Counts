package com.example.clearcounts.ui.screens.Categorias

import androidx.compose.runtime.mutableStateListOf
import com.example.clearcounts.R

object DataSource {
    val categoriasIngresos = mutableStateListOf(
        Categorias(R.drawable.comida,R.string.categoria_comida),
        Categorias(R.drawable.gasolina,R.string.categoria_gasolina),
        Categorias(R.drawable.comida,R.string.categoria_comida),
        Categorias(R.drawable.gasolina,R.string.categoria_gasolina),
    )
    val categoriasGastos = mutableStateListOf(
        Categorias(R.drawable.gasolina,R.string.categoria_gasolina),
        Categorias(R.drawable.comida,R.string.categoria_comida),
        Categorias(R.drawable.gasolina,R.string.categoria_gasolina),
        Categorias(R.drawable.comida,R.string.categoria_comida),

        )
}