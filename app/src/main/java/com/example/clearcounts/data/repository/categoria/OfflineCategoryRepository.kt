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
    override fun getAllCategories() = categoryDao.getAllCategories()
    override fun getCategoriesByType(tipo: String) = categoryDao.getCategoriesByType(tipo)

    override suspend fun insertDefaultCategories() {
        if (categoryDao.getCount() == 0) {
            categoryDao.insertAll(UserDatabase.Companion.DEFAULT_CATEGORIES)
        }
    }

    override suspend fun insertCategoria(categoria: CategoryEntity) {
        categoryDao.insertCategoria(categoria)
    }

}