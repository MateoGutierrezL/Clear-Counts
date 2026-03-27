package com.example.clearcounts.ui.navigation

sealed class Pantallas (val pantalla: String){
    data object Inicio: Pantallas("inicio")
    data object Graficas: Pantallas("graficas")
    data object Perfil: Pantallas("perfil")
    data object Presupuesto: Pantallas("presupuesto")
    data object EditarPerfil: Pantallas("editarPerfil")

    data object Notificaciones: Pantallas("notificaciones")

    data object PreguntasComentarios: Pantallas("preguntasComentarios")

    data object Contactos: Pantallas("contactos")

    data object ingresos: Pantallas("{ruta}/{icono}/{nombre}/{metodoPago}") {
        fun createRoute(ruta: String, icono: String, nombre: String, metodoPago: String) =
            "$ruta/$icono/$nombre/$metodoPago"
    }

    data object Exportar: Pantallas("exportar")

    data object Ajustes: Pantallas("ajustes")

    data object CrearCategoria : Pantallas("crearCategoria/{tipo}")

    object Metas : Pantallas("metas")
    object Detalle : Pantallas("detalle/{tipo}?budgetId={budgetId}") {
        fun createRoute(tipo: String, budgetId: String? = null) =
            if (budgetId != null) "detalle/$tipo?budgetId=$budgetId"
            else "detalle/$tipo"
    }

    object DetalleBudget : Pantallas("detalleBudget/{budgetId}") {
        fun createRoute(budgetId: String) = "detalleBudget/$budgetId"
    }

    data object TodasTransacciones: Pantallas("todasTransacciones")

    data object Idiomas: Pantallas("idiomas")

    object TransaccionesPorMetodo : Pantallas("transaccionesPorMetodo/{metodoPago}") {
        fun createRoute(metodoPago: String) = "transaccionesPorMetodo/$metodoPago"
    }

    data object Recurrentes : Pantallas("recurrentes")
    data object CrearRecurrente : Pantallas("crearRecurrente")
    data object CambiarContrasena : Pantallas("cambiarContrasena")
}