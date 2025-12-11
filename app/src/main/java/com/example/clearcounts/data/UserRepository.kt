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
    fun getUserByEmailStream(correo: String): Flow<UserEntity?>

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

    /**
     * Obtiene el usuario por medio de su id
     */
    fun getUserByIdStream(id: Int): Flow<UserEntity?>

    fun getCurrentLoggedInUser(): Flow<UserEntity?>

    /**
     * Marca a un usuario como logueado guardando su ID en DataStore.
     */
    suspend fun setLoggedInUser(userId: Int)

    /**
     * Cierra la sesión limpiando el ID en DataStore.
     */
    suspend fun logoutUser()

}