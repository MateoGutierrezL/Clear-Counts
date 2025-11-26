package com.example.clearcounts.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import com.example.clearcounts.data.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val USER_DATABASE_NAME = "usuario_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            USER_DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.getUserDao()
}