package com.example.clearcounts.data

import com.example.clearcounts.data.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategories(): Flow<List<CategoryEntity>>

    fun getCategoriesByType(tipo: String): Flow<List<CategoryEntity>>

    suspend fun insertDefaultCategories()

    suspend fun insertCategoria(categoria: CategoryEntity)
}