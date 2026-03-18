package com.example.clearcounts.data.repository.categoria

import com.example.clearcounts.data.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(userId: String): Flow<List<CategoryEntity>>
    fun getCategoriesByType(tipo: String, userId: String): Flow<List<CategoryEntity>>
    suspend fun insertDefaultCategories(userId: String)
    suspend fun insertCategoria(categoria: CategoryEntity)
}