package com.example.clearcounts.ui.screens.Categorias

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.clearcounts.data.database.entities.CategoryEntity
import com.example.clearcounts.utils.CategoryTranslator

@Composable
fun CategoryEntity.getNombreTraducido(): String {
    val resId = CategoryTranslator.getStringRes(icono)
    return if (resId != null) stringResource(resId) else nombre
}