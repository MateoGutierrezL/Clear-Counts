package com.example.clearcounts.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.clearcounts.R
import com.example.clearcounts.data.database.entities.CategoryEntity

object CategoryTranslator {

    fun getStringResByNombre(nombre: String): Int? {
        return when (nombre) {
            "Comida"      -> R.string.cat_comida
            "Transporte"  -> R.string.cat_transporte
            "Salud"       -> R.string.cat_salud
            "Deporte"     -> R.string.cat_deporte
            "Educación"   -> R.string.cat_educacion
            "Ropa"        -> R.string.cat_ropa
            "Alquiler"    -> R.string.cat_alquiler
            "Libros"      -> R.string.cat_libros
            "Maquillaje"  -> R.string.cat_maquillaje
            "Plan datos"  -> R.string.cat_plan_datos
            "Salario"     -> R.string.cat_salario
            "Comision"    -> R.string.cat_comision
            "Inversiones" -> R.string.cat_inversiones
            "Regalo"      -> R.string.cat_regalo
            "Reembolso"   -> R.string.cat_reembolso
            else          -> null
        }
    }

    @Composable
    fun CategoryEntity.getNombreTraducido(): String {
        val resId = getStringResByNombre(nombre)
        return if (resId != null) stringResource(resId) else nombre
    }

    fun Context.traducirCategoria(nombre: String): String {
        val resId = getStringResByNombre(nombre)
        return if (resId != null) getString(resId) else nombre
    }
}