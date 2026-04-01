package com.example.clearcounts.data.local.datastore

import android.util.Log
import com.example.clearcounts.data.local.database.dao.BudgetDao
import com.example.clearcounts.data.local.database.dao.CategoryDao
import com.example.clearcounts.data.local.database.dao.ExpenseDao
import com.example.clearcounts.data.local.database.dao.IncomeDao
import com.example.clearcounts.data.local.database.dao.PaymentMethodDao
import com.example.clearcounts.data.local.database.dao.RecurringDao
import com.example.clearcounts.data.local.database.dao.UserDao
import com.example.clearcounts.data.local.database.entities.UserEntity
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val firestoreSync: FirestoreSyncRepository,
    private val incomeDao: IncomeDao,
    private val expenseDao: ExpenseDao,
    private val budgetDao: BudgetDao,
    private val recurringDao: RecurringDao,
    private val userDao: UserDao,
    private val paymentMethodDao: PaymentMethodDao,
    private val categoryDao: CategoryDao,
    private val syncDataStore: SyncDataStore,
    private val auth: FirebaseAuth
) {
    suspend fun sincronizarDesdeFirestore(emailUsuario: String? = null) {
        val userId = auth.currentUser?.uid ?: return
        val email = emailUsuario ?: auth.currentUser?.email ?: return
        try {
            coroutineScope {
                val ingresos = async { firestoreSync.descargarIngresos(userId) }
                val gastos = async { firestoreSync.descargarGastos(userId) }
                val presupuestos = async { firestoreSync.descargarPresupuesto(userId) }
                val recurrentes = async { firestoreSync.descargarRecurrentes(userId) }
                val metodos = async { firestoreSync.descargarMetodosPago(userId) }
                val categorias = async { firestoreSync.descargarCategorias(userId) }

                ingresos.await().forEach { incomeDao.insert(it) }
                gastos.await().forEach { expenseDao.insert(it) }
                presupuestos.await().forEach { budgetDao.insert(it) }
                recurrentes.await().forEach { recurringDao.insert(it) }
                metodos.await().forEach { paymentMethodDao.insert(it) }
                categorias.await().forEach { categoryDao.insertCategoria(it) }

                val perfilRemoto = async { firestoreSync.descargarUsuario(userId) }
                perfilRemoto.await()?.let { datosRemotos ->
                    Log.d("SYNC", "Perfil remoto: $datosRemotos") // ✅ ¿llegan los datos?
                    userDao.getByEmailOnce(auth.currentUser?.email ?: "")?.let { usuarioLocal ->
                        Log.d("SYNC", "Usuario local antes: $usuarioLocal") // ✅ ¿existe en Room?
                        userDao.update(usuarioLocal.copy(
                            nombre = datosRemotos["nombre"] as? String ?: usuarioLocal.nombre,
                            numero = datosRemotos["numero"] as? String ?: usuarioLocal.numero,
                            avatar = datosRemotos["avatar"] as? String ?: usuarioLocal.avatar
                        ))
                        Log.d("SYNC", "Usuario actualizado con nombre: ${datosRemotos["nombre"]}")
                    } ?: Log.e("SYNC", "Usuario local NO encontrado con email: ${auth.currentUser?.email}")
                }
            }
            actualizarPerfilDesdeFirestore(userId, email)
            syncDataStore.guardarUltimaSync()
            Log.d("SYNC", "Sincronización completa para $userId")
        } catch (e: Exception) {
            Log.e("SYNC", "Error en sincronización: ${e.message}")
        }
    }

    suspend fun actualizarPerfilDesdeFirestore(userId: String, email: String) {
        try {
            val datosRemotos = firestoreSync.descargarUsuario(userId) ?: return

            var usuarioLocal: UserEntity? = null
            repeat(3) {
                usuarioLocal = userDao.getByEmailOnce(email)
                if (usuarioLocal == null) delay(500)
            }

            usuarioLocal?.let { local ->
                val nombreRemoto = datosRemotos["nombre"] as? String
                val numeroRemoto = datosRemotos["numero"] as? String
                val avatarRemoto = datosRemotos["avatar"] as? String

                userDao.update(local.copy(
                    nombre = if (!nombreRemoto.isNullOrBlank()) nombreRemoto else local.nombre,
                    numero = if (!numeroRemoto.isNullOrBlank()) numeroRemoto else local.numero,
                    avatar = if (!avatarRemoto.isNullOrBlank()) avatarRemoto else local.avatar
                ))
                Log.d("SYNC", "Perfil actualizado: nombre=$nombreRemoto, numero=$numeroRemoto")
            } ?: Log.e("SYNC", "No se encontró usuario local con email: $email")
        } catch (e: Exception) {
            Log.e("SYNC", "Error actualizando perfil: ${e.message}")
        }
    }
}