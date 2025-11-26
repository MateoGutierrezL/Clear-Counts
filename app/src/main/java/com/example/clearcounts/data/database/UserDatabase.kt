package com.example.clearcounts.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.clearcounts.data.database.dao.UserDao
import com.example.clearcounts.data.database.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class UserDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
}