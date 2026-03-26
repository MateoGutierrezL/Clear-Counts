package com.example.clearcounts.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.database.entities.UserEntity
import com.example.clearcounts.data.local.datastore.SyncDataStore
import com.example.clearcounts.data.local.repository.usuario.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSessionViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth,
    private val syncDataStore: SyncDataStore
) : ViewModel() {

    //  StateFlow reactivo que escucha cambios en FirebaseAuth
    private val _firebaseUser = MutableStateFlow(auth.currentUser)
    val firebaseUser: FirebaseUser? = auth.currentUser

    val currentUser: StateFlow<UserEntity?> = userRepository.getCurrentLoggedInUser()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )


    //  Escucha cambios de autenticación en tiempo real
    private val authListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _firebaseUser.value = firebaseAuth.currentUser
    }

    init {
        auth.addAuthStateListener(authListener)
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authListener)
    }

    private val _isLoggingOut = MutableStateFlow(false)
    val isLoggingOut: StateFlow<Boolean> = _isLoggingOut.asStateFlow()

    fun logOut(onComplete: () -> Unit) {
        viewModelScope.launch {
            _isLoggingOut.value = true
            try {
                userRepository.signOut()
                syncDataStore.resetUltimaSync()
            } catch (e: Exception) {
                Log.e("Auth", "Error: ${e.message}")
            } finally {
                _isLoggingOut.value = false
                onComplete()
            }
        }
    }

    //  Ahora usa el StateFlow reactivo
    fun getEmail(): String = _firebaseUser.value?.email ?: "Usuario invitado"

}