package com.example.clearcounts.ui.screens.InicioSesion

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

    fun insertUser(userEntity: UserEntity){

        viewModelScope.launch {
            try {
                userRepository.insertUser(userEntity)
                _registerStatus.value = RegistroUsuarioUiState.Success

                println("Usuario ingresado con exito")

            }catch(e: Exception){

                _registerStatus.value = RegistroUsuarioUiState.Error
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