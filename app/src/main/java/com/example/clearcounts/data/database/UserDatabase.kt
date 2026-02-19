package com.example.clearcounts.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clearcounts.data.database.dao.ExpenseDao
import com.example.clearcounts.data.database.dao.IncomeDao
import com.example.clearcounts.data.database.dao.UserDao
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.data.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        IncomeEntity::class,
        ExpenseEntity::class],
    version = 5,
    exportSchema = true
)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getIncomeDao(): IncomeDao

    abstract fun getExpenseDao(): ExpenseDao

}