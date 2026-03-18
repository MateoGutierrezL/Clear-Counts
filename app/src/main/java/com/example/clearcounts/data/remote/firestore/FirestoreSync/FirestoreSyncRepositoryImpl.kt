package com.example.clearcounts.data.remote.firestore.FirestoreSync

import com.example.clearcounts.data.local.database.entities.BudgetEntity
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.example.clearcounts.data.remote.firestore.toBudgetEntity
import com.example.clearcounts.data.remote.firestore.toCategoryEntity
import com.example.clearcounts.data.remote.firestore.toExpenseEntity
import com.example.clearcounts.data.remote.firestore.toIncomeEntity
import com.example.clearcounts.data.remote.firestore.toMap
import com.example.clearcounts.data.remote.firestore.toPaymentMethodEntity
import com.example.clearcounts.data.remote.firestore.toRecurringEntity
import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreSyncRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : FirestoreSyncRepository {

    private fun col(userId: String, nombre: String) =
        firestore.collection("usuarios").document(userId).collection(nombre)

    override suspend fun subirIngreso(userId: String, ingreso: IncomeEntity) {
        col(userId, "ingresos").document(ingreso.id.toString()).set(ingreso.toMap()).await()
    }

    override suspend fun subirGasto(userId: String, gasto: ExpenseEntity) {
        col(userId, "gastos").document(gasto.id.toString()).set(gasto.toMap()).await()
    }

    override suspend fun subirPresupuesto(userId: String, budget: BudgetEntity) {
        col(userId, "presupuesto").document(budget.id.toString()).set(budget.toMap()).await()
    }

    override suspend fun subirRecurrente(userId: String, recurrente: RecurringEntity) {
        col(userId, "recurrentes").document(recurrente.id.toString()).set(recurrente.toMap()).await()
    }

    override suspend fun subirMetodoPago(userId: String, metodo: PaymentMethodEntity) {
        col(userId, "metodos_pago").document(metodo.id.toString()).set(metodo.toMap()).await()
    }

    override suspend fun subirCategoria(userId: String, categoria: CategoryEntity) {
        col(userId, "categorias").document(categoria.id.toString()).set(categoria.toMap()).await()
    }

    override suspend fun eliminarIngreso(userId: String, id: String) {
        col(userId, "ingresos").document(id).delete().await()
    }

    override suspend fun eliminarGasto(userId: String, id: String) {
        col(userId, "gastos").document(id).delete().await()
    }

    override suspend fun eliminarPresupuesto(userId: String, id: String) {
        col(userId, "presupuesto").document(id).delete().await()
    }

    override suspend fun eliminarRecurrente(userId: String, id: String) {
        col(userId, "recurrentes").document(id).delete().await()
    }

    override suspend fun eliminarMetodoPago(userId: String, id: String) {
        col(userId, "metodos_pago").document(id).delete().await()
    }

    override suspend fun descargarIngresos(userId: String) =
        col(userId, "ingresos").get().await().documents.mapNotNull { it.toIncomeEntity() }

    override suspend fun descargarGastos(userId: String) =
        col(userId, "gastos").get().await().documents.mapNotNull { it.toExpenseEntity() }

    override suspend fun descargarPresupuesto(userId: String) =
        col(userId, "presupuesto").get().await().documents.mapNotNull { it.toBudgetEntity() }

    override suspend fun descargarRecurrentes(userId: String) =
        col(userId, "recurrentes").get().await().documents.mapNotNull { it.toRecurringEntity() }

    override suspend fun descargarMetodosPago(userId: String) =
        col(userId, "metodos_pago").get().await().documents.mapNotNull { it.toPaymentMethodEntity() }

    override suspend fun descargarCategorias(userId: String) =
        col(userId, "categorias").get().await().documents.mapNotNull { it.toCategoryEntity() }
}