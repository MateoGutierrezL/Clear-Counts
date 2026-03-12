package com.example.clearcounts.utils

import com.example.clearcounts.R

object CategoryTranslator {

    fun getStringRes(icono: String): Int? {
        return when (icono) {
            "comida"      -> R.string.cat_comida
            "trasnporte"  -> R.string.cat_transporte  // mantiene el typo que tienes en DB
            "salud"       -> R.string.cat_salud
            "deporte"     -> R.string.cat_deporte
            "educacion"   -> R.string.cat_educacion
            "ropa"        -> R.string.cat_ropa
            "alquiler"    -> R.string.cat_alquiler
            "libros"      -> R.string.cat_libros
            "maquillaje"  -> R.string.cat_maquillaje
            "plan_datos"  -> R.string.cat_plan_datos
            "salario"     -> R.string.cat_salario
            "comision"    -> R.string.cat_comision
            "inversiones" -> R.string.cat_inversiones
            "regalo"      -> R.string.cat_regalo
            "reembolso"   -> R.string.cat_reembolso
            else          -> null  // categoría personalizada, usar nombre guardado en DB
        }
    }
}