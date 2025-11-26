package com.example.clearcounts.data

import com.example.clearcounts.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserRepository {

    /**
     * Devuelve todos los datos de usuarios
     */
    fun getAllUsersStream(): Flow<List<UserEntity>>

    /**
     * Devuelve el usuario que tenga el correo especificado
     */
    fun getUserStream(correo: String): Flow<UserEntity?>

    /**
     * Inserta en la base de datos
     */
    suspend fun insertUser(userEntity: UserEntity)

    /**
     * Elimina de la base de datos
     */
    suspend fun deleteUser(userEntity: UserEntity)

    /**
     * Actualiza en la base de datos
     */
    suspend fun updateUser(userEntity: UserEntity)

}