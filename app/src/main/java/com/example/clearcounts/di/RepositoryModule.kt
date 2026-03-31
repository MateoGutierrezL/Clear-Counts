package com.example.clearcounts.di

import com.example.clearcounts.data.local.database.dao.BudgetDao
import com.example.clearcounts.data.local.database.dao.CategoryDao
import com.example.clearcounts.data.local.database.dao.ExpenseDao
import com.example.clearcounts.data.local.database.dao.IncomeDao
import com.example.clearcounts.data.local.database.dao.PaymentMethodDao
import com.example.clearcounts.data.local.database.dao.RecurringDao
import com.example.clearcounts.data.local.database.dao.UserDao
import com.example.clearcounts.data.local.datastore.SyncDataStore
import com.example.clearcounts.data.local.datastore.SyncManager
import com.example.clearcounts.data.local.repository.categoria.CategoryRepository
import com.example.clearcounts.data.local.repository.categoria.OfflineCategoryRepository
import com.example.clearcounts.data.local.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.local.repository.gasto.OfflineExpenseRepository
import com.example.clearcounts.data.local.repository.ingreso.IncomeRepository
import com.example.clearcounts.data.local.repository.ingreso.OfflineIncomeRepository
import com.example.clearcounts.data.local.repository.notificacion.NotificationRepository
import com.example.clearcounts.data.local.repository.notificacion.OfflineNotificationRepository
import com.example.clearcounts.data.local.repository.pago.OfflinePaymentMethodRepository
import com.example.clearcounts.data.local.repository.pago.PaymentMethodRepository
import com.example.clearcounts.data.local.repository.presupuesto.BudgetRepository
import com.example.clearcounts.data.local.repository.presupuesto.OfflineBudgetRepository
import com.example.clearcounts.data.local.repository.recurrente.OfflineRecurringRepository
import com.example.clearcounts.data.local.repository.recurrente.RecurringRepository
import com.example.clearcounts.data.local.repository.usuario.OfflineUserRepository
import com.example.clearcounts.data.local.repository.usuario.UserRepository
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepository
import com.example.clearcounts.data.remote.firestore.FirestoreSync.FirestoreSyncRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Binds
import dagger.Module
import dagger.Provides
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

    @Binds
    @Singleton
    abstract fun bindFirestoreSyncRepository(
        firestoreSyncRepositoryImpl: FirestoreSyncRepositoryImpl
    ): FirestoreSyncRepository

}

@Module
@InstallIn(SingletonComponent::class)
object SyncModule {
    @Provides
    @Singleton
    fun provideSyncManager(
        firestoreSync: FirestoreSyncRepository,
        incomeDao: IncomeDao,
        expenseDao: ExpenseDao,
        budgetDao: BudgetDao,
        recurringDao: RecurringDao,
        userDao: UserDao,
        paymentMethodDao: PaymentMethodDao,
        categoryDao: CategoryDao,
        syncDataStore: SyncDataStore,
        auth: FirebaseAuth
    ): SyncManager = SyncManager(
        firestoreSync, incomeDao, expenseDao, budgetDao,
        recurringDao,userDao, paymentMethodDao, categoryDao, syncDataStore, auth
    )
}