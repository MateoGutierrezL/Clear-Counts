package com.example.clearcounts.data

import com.example.clearcounts.data.database.dao.UserDao
import com.example.clearcounts.data.database.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineUserRepository @Inject constructor(
    private val userDao: UserDao
): UserRepository{

    override fun getAllUsersStream(): Flow<List<UserEntity>> = userDao.getAllUsers()

    override fun getUserByEmailStream(correo: String): Flow<UserEntity?> = userDao.getByEmail(correo)

    override fun getUserByIdStream(id: Int): Flow<UserEntity?> = userDao.getUserById(id)

    override suspend fun insertUser(userEntity: UserEntity) = userDao.insert(userEntity)

    override suspend fun deleteUser(userEntity: UserEntity) = userDao.delete(userEntity)

    override suspend fun updateUser(userEntity: UserEntity) = userDao.update(userEntity)

}