package com.example.clearcounts.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFireBaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    /*
    TODO Si en un futuro se busca usar firestore u otra tecnologia de firebase es necesario
    realizar el mismo proceso que con el FireBaseAuth
     */

}