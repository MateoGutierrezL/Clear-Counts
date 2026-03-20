package com.example.clearcounts.ui.screens.InicioSesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _registerStatus = MutableStateFlow<RegistroUsuarioUiState>(RegistroUsuarioUiState.Loading)
    val registerStatus : StateFlow<RegistroUsuarioUiState> = _registerStatus

    fun signUp(userEntity: UserEntity, contrasenaOriginal: String) { // 👈 Agrega este parámetro
        viewModelScope.launch {
            _registerStatus.value = RegistroUsuarioUiState.Loading
            val result = userRepository.signUp(userEntity.correo, contrasenaOriginal) // 👈 Firebase recibe la original
            result.onSuccess {
                _registerStatus.value = RegistroUsuarioUiState.Success
            }.onFailure {
                _registerStatus.value = RegistroUsuarioUiState.Error
            }
        }

        viewModelScope.launch {
            try {
                userRepository.insertUser(userEntity) // Room guarda el hash
                userRepository.guardarSesion(userEntity.correo)
            } catch(e: Exception) {
                println("Error al ingresar el usuario")
            }
        }
    }
}

sealed interface RegistroUsuarioUiState {
    object Success: RegistroUsuarioUiState
    object Error : RegistroUsuarioUiState
    object Loading : RegistroUsuarioUiState
}