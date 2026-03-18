package com.example.clearcounts.data.remote.firestore.FirestoreSync

import com.example.clearcounts.data.local.database.entities.BudgetEntity
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.example.clearcounts.data.local.database.entities.RecurringEntity

interface FirestoreSyncRepository {
    // Subir individual
    suspend fun subirIngreso(userId: String, ingreso: IncomeEntity)
    suspend fun subirGasto(userId: String, gasto: ExpenseEntity)
    suspend fun subirPresupuesto(userId: String, budget: BudgetEntity)
    suspend fun subirRecurrente(userId: String, recurrente: RecurringEntity)
    suspend fun subirMetodoPago(userId: String, metodo: PaymentMethodEntity)
    suspend fun subirCategoria(userId: String, categoria: CategoryEntity)

    // Eliminar
    suspend fun eliminarIngreso(userId: String, id: String)
    suspend fun eliminarGasto(userId: String, id: String)
    suspend fun eliminarPresupuesto(userId: String, id: String)
    suspend fun eliminarRecurrente(userId: String, id: String)
    suspend fun eliminarMetodoPago(userId: String, id: String)

    // Descargar todos (para cuando inicia sesión)
    suspend fun descargarIngresos(userId: String): List<IncomeEntity>
    suspend fun descargarGastos(userId: String): List<ExpenseEntity>
    suspend fun descargarPresupuesto(userId: String): List<BudgetEntity>
    suspend fun descargarRecurrentes(userId: String): List<RecurringEntity>
    suspend fun descargarMetodosPago(userId: String): List<PaymentMethodEntity>
    suspend fun descargarCategorias(userId: String): List<CategoryEntity>
}