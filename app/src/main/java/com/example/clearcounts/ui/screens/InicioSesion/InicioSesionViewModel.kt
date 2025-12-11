package com.example.clearcounts.ui.screens.InicioSesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModelInicioSesion @Inject constructor(

    private val userRepository: UserRepository
): ViewModel(){

    private val _loginStatus = MutableStateFlow<InicioSesionUiState>(InicioSesionUiState.Loading)
    val loginStatus : StateFlow<InicioSesionUiState> = _loginStatus



    fun validateUser(
        correo: String,
        contrasena: String
    ){

        viewModelScope.launch {

            try {

                val usuario: UserEntity? = userRepository.getUserByEmailStream(correo).firstOrNull()

                if(usuario != null && usuario.contrasena == contrasena){

                    userRepository.setLoggedInUser(usuario.id)

                    _loginStatus.value = InicioSesionUiState.Success(usuario.id)
                    println("Inicio de sesion exitoso")

                }else{

                    _loginStatus.value = InicioSesionUiState.Error
                    println("Usuario o contraseña incorrectos")
                }

            }catch (e: Exception){

                _loginStatus.value = InicioSesionUiState.Error
                println("Error al iniciar sesion")
            }
        }
    }

    fun clearLoginStatus() {
        _loginStatus.value = InicioSesionUiState.Loading // O un nuevo objeto Idle
    }

}

sealed interface InicioSesionUiState {
    data class Success(val Userid: Int): InicioSesionUiState

    object Error : InicioSesionUiState
    object Loading : InicioSesionUiState
}