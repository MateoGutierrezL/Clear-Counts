package com.example.clearcounts.ui

sealed class Pantallas (val pantalla: String){
    data object Inicio: Pantallas("inicio")
    data object Graficas: Pantallas("graficas")
    data object Perfil: Pantallas("perfil")
    data object Presupuesto: Pantallas("presupuesto")

    data object EditarPerfil: Pantallas("editarPerfil")

}