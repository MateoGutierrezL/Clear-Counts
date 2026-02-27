package com.example.clearcounts.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.repository.usuario.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    val firebaseUser: FirebaseUser? = auth.currentUser

    val currentUser: StateFlow<UserEntity?> = userRepository.getCurrentLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun logOut(onComplete:() -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.signOut()
                onComplete()
            } catch (e: Exception){
                Log.e("Auth", "Error: ${e.message}")
                onComplete()
            }

        }
    }

    fun getEmail(): String = firebaseUser?.email ?: "Usuario invitado"

}