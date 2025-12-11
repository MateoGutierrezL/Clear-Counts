package com.example.clearcounts.data

import com.example.clearcounts.data.database.dao.UserDao
import com.example.clearcounts.data.database.entities.UserEntity
import com.example.clearcounts.data.datastore.UserSessionDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class OfflineUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val sessionDataStore: UserSessionDataStore
): UserRepository{

    override fun getAllUsersStream(): Flow<List<UserEntity>> = userDao.getAllUsers()

    override fun getUserByEmailStream(correo: String): Flow<UserEntity?> = userDao.getByEmail(correo)

    override fun getUserByIdStream(id: Int): Flow<UserEntity?> = userDao.getUserById(id)

    override suspend fun insertUser(userEntity: UserEntity) = userDao.insert(userEntity)

    override suspend fun deleteUser(userEntity: UserEntity) = userDao.delete(userEntity)

    override suspend fun updateUser(userEntity: UserEntity) = userDao.update(userEntity)

    override fun getCurrentLoggedInUser(): Flow<UserEntity?> {
        return sessionDataStore.loggedInUserIdFlow
            .flatMapLatest { userId ->
                if (userId != -1) {
                    userDao.getUserById(userId)
                } else {
                    flowOf(null)
                }
            }
    }

    override suspend fun setLoggedInUser(userId: Int) {
        sessionDataStore.setLoggedInUserId(userId)
    }

    override suspend fun logoutUser() {
        sessionDataStore.clearLoggedInUserId()
    }

}