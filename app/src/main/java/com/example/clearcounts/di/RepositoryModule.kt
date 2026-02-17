package com.example.clearcounts.di

import com.example.clearcounts.data.ExpenseRepository
import com.example.clearcounts.data.IncomeRepository
import com.example.clearcounts.data.OfflineExpenseRepository
import com.example.clearcounts.data.OfflineIncomeRepository
import com.example.clearcounts.data.OfflineUserRepository
import com.example.clearcounts.data.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        offlineUserRepository: OfflineUserRepository
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindIncomeRepository(
        offlineIncomeRepository: OfflineIncomeRepository
    ): IncomeRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        offlineExpenseRepository: OfflineExpenseRepository
    ): ExpenseRepository
}