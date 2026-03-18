package com.example.clearcounts.data.remote.firestore

import com.example.clearcounts.data.local.database.entities.BudgetEntity
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.google.firebase.firestore.DocumentSnapshot

// IncomeEntity
fun IncomeEntity.toMap() = mapOf(
    "id" to id, "userId" to userId, "categoria" to categoria,
    "cantidad" to cantidad, "hora" to hora, "fecha" to fecha,
    "nota" to nota, "metodoPago" to metodoPago
)
fun DocumentSnapshot.toIncomeEntity() = try {
    IncomeEntity(
        id = getString("id")?.toIntOrNull() ?: 0,
        userId = getString("userId") ?: "",
        categoria = getString("categoria") ?: "",
        cantidad = getDouble("cantidad") ?: 0.0,
        hora = getString("hora") ?: "",
        fecha = getString("fecha") ?: "",
        nota = getString("nota"),
        metodoPago = getString("metodoPago") ?: "Efectivo"
    )
} catch (e: Exception) { null }

// ExpenseEntity
fun ExpenseEntity.toMap() = mapOf(
    "id" to id, "userId" to userId, "categoria" to categoria,
    "cantidad" to cantidad, "hora" to hora, "fecha" to fecha,
    "nota" to nota, "metodoPago" to metodoPago
)
fun DocumentSnapshot.toExpenseEntity() = try {
    ExpenseEntity(
        id = getString("id")?.toIntOrNull() ?: 0,
        userId = getString("userId") ?: "",
        categoria = getString("categoria") ?: "",
        cantidad = getDouble("cantidad") ?: 0.0,
        hora = getString("hora") ?: "",
        fecha = getString("fecha") ?: "",
        nota = getString("nota"),
        metodoPago = getString("metodoPago") ?: "Efectivo"
    )
} catch (e: Exception) { null }

// BudgetEntity
fun BudgetEntity.toMap() = mapOf(
    "id" to id, "userId" to userId, "nombre" to nombre,
    "tipo" to tipo, "cantidadRequerida" to cantidadRequerida,
    "cantidadAcumulada" to cantidadAcumulada, "prestador" to prestador,
    "fechaInicio" to fechaInicio, "fechaLimite" to fechaLimite, "nota" to nota
)
fun DocumentSnapshot.toBudgetEntity() = try {
    BudgetEntity(
        id = getString("id")?.toIntOrNull() ?: 0,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        tipo = getString("tipo") ?: "",
        cantidadRequerida = getDouble("cantidadRequerida") ?: 0.0,
        cantidadAcumulada = getDouble("cantidadAcumulada") ?: 0.0,
        prestador = getString("prestador"),
        fechaInicio = getString("fechaInicio"),
        fechaLimite = getString("fechaLimite"),
        nota = getString("nota")
    )
} catch (e: Exception) { null }

// RecurringEntity
fun RecurringEntity.toMap() = mapOf(
    "id" to id, "userId" to userId, "nombre" to nombre,
    "categoria" to categoria, "cantidad" to cantidad, "tipo" to tipo,
    "diaDelMes" to diaDelMes, "activo" to activo,
    "metodoPago" to metodoPago, "ultimaEjecucion" to ultimaEjecucion
)
fun DocumentSnapshot.toRecurringEntity() = try {
    RecurringEntity(
        id = getString("id")?.toIntOrNull() ?: 0,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        categoria = getString("categoria") ?: "",
        cantidad = getDouble("cantidad") ?: 0.0,
        tipo = getString("tipo") ?: "",
        diaDelMes = getLong("diaDelMes")?.toInt() ?: 1,
        activo = getBoolean("activo") ?: true,
        metodoPago = getString("metodoPago") ?: "Efectivo",
        ultimaEjecucion = getString("ultimaEjecucion")
    )
} catch (e: Exception) { null }

// PaymentMethodEntity
fun PaymentMethodEntity.toMap() = mapOf(
    "id" to id, "userId" to userId, "nombre" to nombre,
    "icono" to icono, "esDefault" to esDefault
)
fun DocumentSnapshot.toPaymentMethodEntity() = try {
    PaymentMethodEntity(
        id = getString("id")?.toIntOrNull() ?: 0,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        icono = getString("icono") ?: "",
        esDefault = getBoolean("esDefault") ?: false
    )
} catch (e: Exception) { null }

// CategoryEntity
fun CategoryEntity.toMap() = mapOf(
    "id" to id, "userId" to userId, "nombre" to nombre,
    "icono" to icono, "tipo" to tipo
)
fun DocumentSnapshot.toCategoryEntity() = try {
    CategoryEntity(
        id = getString("id")?.toIntOrNull() ?: 0,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        icono = getString("icono") ?: "",
        tipo = getString("tipo") ?: ""
    )
} catch (e: Exception) { null }