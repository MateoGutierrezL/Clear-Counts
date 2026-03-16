package com.example.clearcounts.di

import com.example.clearcounts.data.repository.categoria.CategoryRepository
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.repository.categoria.OfflineCategoryRepository
import com.example.clearcounts.data.repository.gasto.OfflineExpenseRepository
import com.example.clearcounts.data.repository.ingreso.OfflineIncomeRepository
import com.example.clearcounts.data.repository.usuario.OfflineUserRepository
import com.example.clearcounts.data.repository.usuario.UserRepository
import com.example.clearcounts.data.database.UserDatabase
import com.example.clearcounts.data.repository.notificacion.NotificationRepository
import com.example.clearcounts.data.repository.notificacion.OfflineNotificationRepository
import com.example.clearcounts.data.repository.pago.OfflinePaymentMethodRepository
import com.example.clearcounts.data.repository.pago.PaymentMethodRepository
import com.example.clearcounts.data.repository.presupuesto.BudgetRepository
import com.example.clearcounts.data.repository.presupuesto.OfflineBudgetRepository
import com.example.clearcounts.data.repository.recurrente.OfflineRecurringRepository
import com.example.clearcounts.data.repository.recurrente.RecurringRepository
import com.google.firebase.sessions.dagger.Provides

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

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        offlineCategoryRepository: OfflineCategoryRepository
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        offlineNotificationRepository: OfflineNotificationRepository
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        offlineBudgetRepository: OfflineBudgetRepository
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindPaymentMethodRepository(
        offlinePaymentMethodRepository: OfflinePaymentMethodRepository
    ): PaymentMethodRepository

    @Binds
    @Singleton
    abstract fun bindRecurringRepository(
        offlineRecurringRepository: OfflineRecurringRepository
    ): RecurringRepository

}