package com.example.clearcounts.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clearcounts.data.local.database.dao.BudgetDao
import com.example.clearcounts.data.local.database.dao.CategoryDao
import com.example.clearcounts.data.local.database.dao.ExpenseDao
import com.example.clearcounts.data.local.database.dao.IncomeDao
import com.example.clearcounts.data.local.database.dao.NotificationDao
import com.example.clearcounts.data.local.database.dao.PaymentMethodDao
import com.example.clearcounts.data.local.database.dao.RecurringDao
import com.example.clearcounts.data.local.database.dao.UserDao
import com.example.clearcounts.data.local.database.entities.BudgetEntity
import com.example.clearcounts.data.local.database.entities.CategoryEntity
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.data.local.database.entities.NotificationEntity
import com.example.clearcounts.data.local.database.entities.PaymentMethodEntity
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.example.clearcounts.data.local.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        IncomeEntity::class,
        ExpenseEntity::class,
        CategoryEntity::class,
        NotificationEntity::class,
        BudgetEntity::class,
        PaymentMethodEntity::class,
        RecurringEntity::class],
    version = 22,
    exportSchema = true
)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getIncomeDao(): IncomeDao

    abstract fun getExpenseDao(): ExpenseDao

    abstract fun notificationDao(): NotificationDao

    abstract fun categoryDao(): CategoryDao

    abstract fun paymentMethodDao(): PaymentMethodDao

    abstract fun recurringDao(): RecurringDao

    abstract fun budgetDao(): BudgetDao

        companion object {
            // Categorías por defecto
            val DEFAULT_CATEGORIES = listOf(
                // GASTOS
                CategoryEntity(nombre = "Comida", icono = "comida", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Transporte", icono = "bus", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Salud", icono = "hospital", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Deporte", icono = "balon", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Educación", icono = "birrete", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Ropa", icono = "ropa", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Alquiler", icono = "casa", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Libros", icono = "libros", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Maquillaje", icono = "maquillaje", tipo = "gasto", esDefault = true),
                CategoryEntity(nombre = "Plan datos", icono = "celular", tipo = "gasto", esDefault = true),
                // INGRESOS
                CategoryEntity(nombre = "Salario", icono = "dinero", tipo = "ingreso", esDefault = true),
                CategoryEntity(nombre = "Comision", icono = "cartera", tipo = "ingreso", esDefault = true),
                CategoryEntity(nombre = "Inversiones", icono = "alcancia", tipo = "ingreso", esDefault = true),
                CategoryEntity(nombre = "Regalo", icono = "regalo", tipo = "ingreso", esDefault = true),
                CategoryEntity(nombre = "Reembolso", icono = "reembolso", tipo = "ingreso", esDefault = true),
            )
            val DEFAULT_PAYMENT_METHODS = listOf(
                PaymentMethodEntity(nombre = "Efectivo", icono = "efectivo", esDefault = true),
                PaymentMethodEntity(nombre = "Débito", icono = "tarjetaazul", esDefault = true),
                PaymentMethodEntity(nombre = "Crédito", icono = "tarjetaroja", esDefault = true),
            )

        }

}