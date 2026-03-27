package com.example.clearcounts.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.clearcounts.data.local.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao{

    @Query("SELECT * FROM usuario")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM usuario WHERE correo = :correo")
    fun getByEmail(correo: String): Flow<UserEntity>

    //  NUEVO — query directa sin Flow para uso en suspend functions
    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    suspend fun getByEmailOnce(correo: String): UserEntity?

    //  NUEVO — para obtener el ID del último insertado
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAndGetId(userEntity: UserEntity): Long

    @Query("SELECT * FROM usuario where id = :id")
    fun getUserById(id: Int): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userEntity: UserEntity)

    @Update
    suspend fun update(userEntity: UserEntity)

    @Delete
    suspend fun delete(userEntity: UserEntity)

}