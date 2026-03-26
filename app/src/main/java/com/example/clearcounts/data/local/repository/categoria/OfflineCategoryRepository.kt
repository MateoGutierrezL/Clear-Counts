package com.example.clearcounts.data.local.repository.categoria

import android.util.Log
import com.example.clearcounts.data.local.database.UserDatabase
import com.example.clearcounts.data.local.database.dao.CategoryDao
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import jakarta.inject.Inject

class OfflineCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val firestoreSync: FirestoreSyncRepository,
    private val auth: FirebaseAuth
) : CategoryRepository {

    private val userId get() = auth.currentUser?.uid ?: ""

    override fun getAllCategories(userId: String) = categoryDao.getAllCategories(userId)
    override fun getCategoriesByType(tipo: String, userId: String) =
        categoryDao.getCategoriesByType(tipo, userId)


    override suspend fun insertDefaultCategories(userId: String) {
        UserDatabase.DEFAULT_CATEGORIES.forEach { categoria ->
            val existe = categoryDao.existsByNameAndType(categoria.nombre, categoria.tipo, userId)
            if (!existe) {
                val nueva = categoria.copy(userId = userId)
                categoryDao.insertCategoria(nueva)
                try {
                    firestoreSync.subirCategoria(userId, nueva)
                } catch (e: Exception) {
                    Log.e("SYNC", "Error subiendo categoría: ${e.message}")
                }
            }
        }
    }

    override suspend fun insertCategoria(categoria: CategoryEntity) {
        categoryDao.insertCategoria(categoria)
        firestoreSync.subirCategoria(userId, categoria)
    }

    override suspend fun insertCategoriaLocal(categoria: CategoryEntity) {
        categoryDao.insertCategoria(categoria)
    }
}