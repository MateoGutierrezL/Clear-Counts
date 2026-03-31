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
    "firestoreId" to firestoreId,
    "userId" to userId,
    "categoria" to categoria,
    "cantidad" to cantidad,
    "hora" to hora,
    "fecha" to fecha,
    "nota" to nota,
    "metodoPago" to metodoPago
)

fun DocumentSnapshot.toIncomeEntity() = try {
    IncomeEntity(
        firestoreId = getString("firestoreId") ?: id,  // ✅ getLong, no getString
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
fun DocumentSnapshot.toExpenseEntity() = try {
    ExpenseEntity(
        firestoreId = getString("firestoreId") ?: id,
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
fun DocumentSnapshot.toBudgetEntity() = try {
    BudgetEntity(
        firestoreId = getString("firestoreId") ?: id,
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
fun DocumentSnapshot.toRecurringEntity() = try {
    RecurringEntity(
        firestoreId = getString("firestoreId") ?: id,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        categoria = getString("categoria") ?: "",
        cantidad = getDouble("cantidad") ?: 0.0,
        tipo = getString("tipo") ?: "",
        frecuencia = getString("frecuencia") ?: "mensual",
        diaReferencia = getLong("diaReferencia")?.toInt() ?: 1,
        activo = getBoolean("activo") ?: true,
        metodoPago = getString("metodoPago") ?: "Efectivo",
        ultimaEjecucion = getString("ultimaEjecucion")
    )
} catch (e: Exception) { null }

// PaymentMethodEntity
fun DocumentSnapshot.toPaymentMethodEntity() = try {
    PaymentMethodEntity(
        firestoreId = getString("firestoreId") ?: id,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        icono = getString("icono") ?: "",
        esDefault = getBoolean("esDefault") ?: false
    )
} catch (e: Exception) { null }

// CategoryEntity
fun DocumentSnapshot.toCategoryEntity() = try {
    CategoryEntity(
        firestoreId = getString("firestoreId") ?: id,
        userId = getString("userId") ?: "",
        nombre = getString("nombre") ?: "",
        icono = getString("icono") ?: "",
        tipo = getString("tipo") ?: ""
    )
} catch (e: Exception) { null }

// ExpenseEntity
fun ExpenseEntity.toMap() = mapOf(
    "firestoreId" to firestoreId,
    "userId" to userId,
    "categoria" to categoria,
    "cantidad" to cantidad,
    "hora" to hora,
    "fecha" to fecha,
    "nota" to nota,
    "metodoPago" to metodoPago
)

// BudgetEntity
fun BudgetEntity.toMap() = mapOf(
    "firestoreId" to firestoreId,
    "userId" to userId,
    "nombre" to nombre,
    "tipo" to tipo,
    "cantidadRequerida" to cantidadRequerida,
    "cantidadAcumulada" to cantidadAcumulada,
    "prestador" to prestador,
    "fechaInicio" to fechaInicio,
    "fechaLimite" to fechaLimite,
    "nota" to nota
)

// RecurringEntity
fun RecurringEntity.toMap() = mapOf(
    "firestoreId" to firestoreId,
    "userId" to userId,
    "nombre" to nombre,
    "categoria" to categoria,
    "cantidad" to cantidad,
    "tipo" to tipo,
    "frecuencia" to frecuencia,
    "diaReferencia" to diaReferencia,
    "activo" to activo,
    "metodoPago" to metodoPago,
    "ultimaEjecucion" to ultimaEjecucion
)

// PaymentMethodEntity
fun PaymentMethodEntity.toMap() = mapOf(
    "firestoreId" to firestoreId,
    "userId" to userId,
    "nombre" to nombre,
    "icono" to icono,
    "esDefault" to esDefault
)

// CategoryEntity
fun CategoryEntity.toMap() = mapOf(
    "firestoreId" to firestoreId,
    "userId" to userId,
    "nombre" to nombre,
    "icono" to icono,
    "tipo" to tipo
)