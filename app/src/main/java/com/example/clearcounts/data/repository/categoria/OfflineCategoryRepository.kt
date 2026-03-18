package com.example.clearcounts.data.repository.categoria

import com.example.clearcounts.data.database.UserDatabase
import com.example.clearcounts.data.database.dao.CategoryDao
import com.example.clearcounts.data.database.dao.CategoryExpenseSummary
import com.example.clearcounts.data.database.entities.CategoryEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class OfflineCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategories(userId: String) = categoryDao.getAllCategories(userId)
    override fun getCategoriesByType(tipo: String, userId: String) =
        categoryDao.getCategoriesByType(tipo, userId)

    override suspend fun insertDefaultCategories(userId: String) {
        if (categoryDao.getCountByUser(userId) == 0) {
            categoryDao.insertAll(
                UserDatabase.DEFAULT_CATEGORIES.map { it.copy(userId = userId) }
            )
        }
    }

    override suspend fun insertCategoria(categoria: CategoryEntity) {
        categoryDao.insertCategoria(categoria)
    }
}