package com.example.clearcounts.ui.screens.InicioSesion

import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
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

    fun signUp(userEntity: UserEntity) {
        viewModelScope.launch {
            _registerStatus.value = RegistroUsuarioUiState.Loading

            val result = userRepository.signUp(userEntity.correo, userEntity.contrasena)

            result.onSuccess {
                _registerStatus.value = RegistroUsuarioUiState.Success
            }.onFailure {
                _registerStatus.value = RegistroUsuarioUiState.Error
            }
        }

        viewModelScope.launch {
            try {
                userRepository.insertUser(userEntity)

                println("Usuario ingresado con exito")

            }catch(e: Exception){

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