package com.example.clearcounts.data.local.repository.usuario

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.example.clearcounts.data.local.database.dao.UserDao
import com.example.clearcounts.data.local.database.entities.UserEntity
import com.example.clearcounts.data.local.datastore.SyncManager
import com.example.clearcounts.data.local.datastore.UserSessionDataStore
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.facebook.login.LoginManager
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OfflineUserRepository @Inject constructor(
    private val userDao: UserDao,
    private val sessionDataStore: UserSessionDataStore,
    private val auth: FirebaseAuth,
    private val firestoreSync: FirestoreSyncRepository,
    private val syncManager: SyncManager,
    @ApplicationContext private val context: Context
): UserRepository {

    override fun getAllUsersStream(): Flow<List<UserEntity>> = userDao.getAllUsers()

    override fun getUserByEmailStream(correo: String): Flow<UserEntity?> = userDao.getByEmail(correo)

    override fun getUserByIdStream(id: Int): Flow<UserEntity?> = userDao.getUserById(id)

    override suspend fun insertUser(userEntity: UserEntity) = userDao.insert(userEntity)

    override suspend fun deleteUser(userEntity: UserEntity) = userDao.delete(userEntity)

    override suspend fun updateUser(userEntity: UserEntity) {
        userDao.update(userEntity)
        try {
            val uid = auth.currentUser?.uid ?: return
            firestoreSync.subirUsuario(uid, userEntity)
        } catch (e: Exception) {
            Log.e("SYNC", "Error actualizando usuario en Firestore: ${e.message}")
        }
    }

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
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            //  Guardar el ID en DataStore
            guardarUsuarioEnSession(email)
            syncManager.sincronizarDesdeFirestore(emailUsuario = email)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInFacebook(token: String): Result<AuthResult> {
        return try {
            val credential = FacebookAuthProvider.getCredential(token)
            val result = auth.signInWithCredential(credential).await()
            val email = result.user?.email
            if (email != null) {
                guardarUsuarioEnSession(email, proveedor = "facebook")
            } else {
                val uid = result.user?.uid ?: ""
                val displayName = result.user?.displayName ?: "Usuario"
                val usuarioLocal = userDao.getAllUsers().firstOrNull()
                    ?.find { it.nombre == displayName }
                if (usuarioLocal == null) {
                    val nuevoUsuario = UserEntity(
                        id = 0,
                        nombre = displayName,
                        numero = "",
                        correo = uid,
                        contrasena = "",
                        avatar = "perro"
                    )
                    userDao.insert(nuevoUsuario)
                    userDao.getByEmail(uid).firstOrNull()?.let { user ->
                        sessionDataStore.setLoggedInUserId(user.id)
                    }
                } else {
                    sessionDataStore.setLoggedInUserId(usuarioLocal.id)
                }
            }
            syncManager.sincronizarDesdeFirestore(emailUsuario = email)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInGoogle(token: String, email: String?): Result<AuthResult> {
        return try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val res = auth.signInWithCredential(credential).await()

            // Prioridad: email del ViewModel > auth.currentUser > res.user
            val emailFinal = email
                ?: auth.currentUser?.email
                ?: res.user?.email

            Log.d("DEBUG_SESSION", "signInGoogle - email: $emailFinal")

            if (emailFinal != null) {
                guardarUsuarioEnSession(emailFinal, proveedor = "google")
                syncManager.sincronizarDesdeFirestore(emailUsuario = emailFinal)
            } else {
                Log.e("DEBUG_SESSION", "No se pudo obtener email en signInGoogle")
            }

            Result.success(res)
        } catch (e: Exception) {
            Log.e("DEBUG_SESSION", "Error en signInGoogle: ${e.message}")
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
            Log.e("Auth", "Funciona")
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

    private suspend fun guardarUsuarioEnSession(email: String, proveedor: String = "email") {
        val usuarioLocal = userDao.getByEmailOnce(email)
        Log.d("Auth", "Usuario en Room: $usuarioLocal")
        if (usuarioLocal != null) {
            Log.d("Auth", "Guardando ID: ${usuarioLocal.id}")
            val firebaseUser = auth.currentUser
            val nombreActualizado = firebaseUser?.displayName
            if (!nombreActualizado.isNullOrBlank() &&
                nombreActualizado != usuarioLocal.nombre) {
                userDao.update(usuarioLocal.copy(nombre = nombreActualizado))
            }
            sessionDataStore.setLoggedInUserId(usuarioLocal.id)
        } else {
            Log.d("Auth", "Usuario no existe, creando nuevo...")
            val firebaseUser = auth.currentUser
            val nuevoUsuario = UserEntity(
                id = 0,
                nombre = firebaseUser?.displayName
                    ?: email.substringBefore("@"),
                numero = "",
                correo = email,
                contrasena = "",
                avatar = "cacatuaninfa",
                proveedor = proveedor
            )

            val newId = userDao.insertAndGetId(nuevoUsuario)
            if (newId != -1L) {
                sessionDataStore.setLoggedInUserId(newId.toInt())
            } else {
                userDao.getByEmailOnce(email)?.let { user ->
                    sessionDataStore.setLoggedInUserId(user.id)
                }
            }
        }
    }

    override suspend fun guardarSesion(email: String) {
        guardarUsuarioEnSession(email)
    }

    override suspend fun getUserByEmail(correo: String): UserEntity? {
        return userDao.getByEmailOnce(correo)
    }

}