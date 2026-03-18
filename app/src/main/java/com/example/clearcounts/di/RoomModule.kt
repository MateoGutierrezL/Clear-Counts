package com.example.clearcounts.di

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.clearcounts.data.local.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val USER_DATABASE_NAME = "usuario_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            USER_DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Singleton
    @Provides
    fun provideUserDao(db: UserDatabase) = db.getUserDao()

    @Singleton
    @Provides
    fun provideIncomeDao(db: UserDatabase) = db.getIncomeDao()

    @Singleton
    @Provides
    fun provideExpenseDao(db: UserDatabase) = db.getExpenseDao()

    @Singleton
    @Provides
    fun provideCategoryDao(db: UserDatabase) = db.categoryDao()

    @Singleton
    @Provides
    fun provideNotificationDao(db: UserDatabase) = db.notificationDao()

    @Singleton
    @Provides
    fun provideBudgetDao(db: UserDatabase) = db.budgetDao()

    @Singleton
    @Provides
    fun providePaymentMethodDao(db: UserDatabase) = db.paymentMethodDao()

    @Provides
    @Singleton
    fun provideRecurringDao(db: UserDatabase) = db.recurringDao()

}