package com.example.clearcounts.data.local.repository.usuario

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.example.clearcounts.data.local.database.dao.UserDao
import com.example.clearcounts.data.local.database.entities.UserEntity
import com.example.clearcounts.data.local.datastore.UserSessionDataStore
import com.facebook.login.LoginManager
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OfflineUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val sessionDataStore: UserSessionDataStore,
    private val auth: FirebaseAuth,
    @ApplicationContext private val context: Context
): UserRepository {

    override fun getAllUsersStream(): Flow<List<UserEntity>> = userDao.getAllUsers()

    override fun getUserByEmailStream(correo: String): Flow<UserEntity?> = userDao.getByEmail(correo)

    override fun getUserByIdStream(id: Int): Flow<UserEntity?> = userDao.getUserById(id)

    override suspend fun insertUser(userEntity: UserEntity) = userDao.insert(userEntity)

    override suspend fun deleteUser(userEntity: UserEntity) = userDao.delete(userEntity)

    override suspend fun updateUser(userEntity: UserEntity) = userDao.update(userEntity)

    @OptIn(ExperimentalCoroutinesApi::class)
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

    override suspend fun logoutUser() {
        sessionDataStore.clearLoggedInUserId()
    }

    override suspend fun signUp(email: String, password: String): Result<Boolean> {
        return try{
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(true)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Boolean>{

        return try{

            val result = auth.signInWithEmailAndPassword(email, password).await()

            Result.success(true)
        } catch (e: Exception){

            Result.failure(e)
        }

    }

    override suspend fun signInFacebook(token: String): Result<AuthResult> {
        return try {
            val credential = FacebookAuthProvider.getCredential(token)
            val result = auth.signInWithCredential(credential).await()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInGoogle(token: String): Result<AuthResult>{
        return try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val res = auth.signInWithCredential(credential).await()
            Result.success(res)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun signOut() {

        auth.signOut()

        LoginManager.getInstance().logOut()

        sessionDataStore.clearLoggedInUserId()

        try {
            val credentialManager = CredentialManager.Companion.create(context)
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            Log.e("AUth", "Funciona")
        } catch (e: Exception) {
            // Loguear error o ignorar si falla la limpieza de estado
            Log.e("Auth", "Error al limpiar estado de credenciales: ${e.message}")
        }
    }

    override suspend fun SendResetPassword(email: String): Result<Unit> {

        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

}