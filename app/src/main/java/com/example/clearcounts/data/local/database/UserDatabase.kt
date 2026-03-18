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
    version = 17,
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
                CategoryEntity(nombre = "Comida", icono = "comida", tipo = "gasto"),
                CategoryEntity(nombre = "Transporte", icono = "trasnporte", tipo = "gasto"),
                CategoryEntity(nombre = "Salud", icono = "salud", tipo = "gasto"),
                CategoryEntity(nombre = "Deporte", icono = "deporte", tipo = "gasto"),
                CategoryEntity(nombre = "Educación", icono = "educacion", tipo = "gasto"),
                CategoryEntity(nombre = "Ropa", icono = "ropa", tipo = "gasto"),
                CategoryEntity(nombre = "Alquiler", icono = "alquiler", tipo = "gasto"),
                CategoryEntity(nombre = "Libros", icono = "libros", tipo = "gasto"),
                CategoryEntity(nombre = "Maquillaje", icono = "maquillaje", tipo = "gasto"),
                CategoryEntity(nombre = "Plan datos", icono = "plan_datos", tipo = "gasto"),
                // INGRESOS
                CategoryEntity(nombre = "Salario", icono = "salario", tipo = "ingreso"),
                CategoryEntity(nombre = "Comision", icono = "comision", tipo = "ingreso"),
                CategoryEntity(nombre = "Inversiones", icono = "inversiones", tipo = "ingreso"),
                CategoryEntity(nombre = "Regalo", icono = "regalo", tipo = "ingreso"),
                CategoryEntity(nombre = "Reembolso", icono = "reembolso", tipo = "ingreso"),
            )
            val DEFAULT_PAYMENT_METHODS = listOf(
                PaymentMethodEntity(nombre = "Efectivo", icono = "efectivo", esDefault = true),
                PaymentMethodEntity(nombre = "Débito", icono = "debito", esDefault = true),
                PaymentMethodEntity(nombre = "Crédito", icono = "credito", esDefault = true),
            )

        }

}