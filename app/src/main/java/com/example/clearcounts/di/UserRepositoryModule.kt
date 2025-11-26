package com.example.clearcounts.di

import com.example.clearcounts.data.OfflineUserRepository
import com.example.clearcounts.data.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UserRepositoryModule {

    @Binds
    abstract fun bindUserRepository(
        // Hilt ya sabe cómo construir esta clase (por su @Inject constructor)
        offlineUserRepository: OfflineUserRepository
    ): UserRepository
}