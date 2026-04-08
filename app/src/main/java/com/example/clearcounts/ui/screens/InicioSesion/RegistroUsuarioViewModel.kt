package com.example.clearcounts.ui.screens.InicioSesion

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.R
import com.example.clearcounts.data.local.database.entities.UserEntity
import com.example.clearcounts.data.local.repository.usuario.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegistroUsuarioViewModel @Inject constructor(

    private val userRepository: UserRepository
): ViewModel() {



    // En tu ViewModel
    private val _registerStatus = MutableStateFlow<RegistroUsuarioUiState>(RegistroUsuarioUiState.Idle)
    val registerStatus: StateFlow<RegistroUsuarioUiState> = _registerStatus

    fun signUp(userEntity: UserEntity, contrasenaOriginal: String) {
        if (_registerStatus.value is RegistroUsuarioUiState.Loading) return

        viewModelScope.launch {
            _registerStatus.value = RegistroUsuarioUiState.Loading

            val result = userRepository.signUp(userEntity.correo, contrasenaOriginal)

            result.onSuccess {
                try {
                    userRepository.insertUser(userEntity)
                    userRepository.guardarSesion(userEntity.correo)
                    _registerStatus.value = RegistroUsuarioUiState.Success
                } catch (e: Exception) {
                    _registerStatus.value = RegistroUsuarioUiState.Error("Error al guardar localmente")
                }
            }
            result.onFailure { exception ->
                val errorType = when {
                    exception.message?.contains("ALREADY_EXISTS") == true ||
                            exception.message?.contains("already in use") == true -> "EMAIL_EXISTS"
                    else -> "UNKNOWN"
                }
                _registerStatus.value = RegistroUsuarioUiState.Error(errorType)
            }
        }
    }
    fun resetStatus() {
        _registerStatus.value = RegistroUsuarioUiState.Idle
    }
}

sealed interface RegistroUsuarioUiState {
    object Success: RegistroUsuarioUiState
    data class Error(val mensaje: String) : RegistroUsuarioUiState
    object Loading : RegistroUsuarioUiState
    object Idle : RegistroUsuarioUiState
}