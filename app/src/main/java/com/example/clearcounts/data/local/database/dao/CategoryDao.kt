package com.example.clearcounts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE user_id = :userId")
    fun getAllCategories(userId: String): Flow<List<CategoryEntity>>

    @Query("SELECT COUNT(*) FROM categories WHERE user_id = :userId")
    suspend fun getCountByUser(userId: String): Int

    @Query("SELECT * FROM categories WHERE tipo = :tipo AND user_id = :userId")
    fun getCategoriesByType(tipo: String, userId: String): Flow<List<CategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoria(categoria: CategoryEntity)
}