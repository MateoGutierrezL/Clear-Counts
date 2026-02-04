package com.example.clearcounts.ui.screens.InicioSesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.InicioUsuario
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
        email: String,
        password: String
    ){

        viewModelScope.launch {

            _loginStatus.value = InicioSesionUiState.Loading

            val result = userRepository.signIn(email, password)

            result.onSuccess {
                _loginStatus.value = InicioSesionUiState.Success
            }.onFailure {
                _loginStatus.value = InicioSesionUiState.Error
            }
        }
    }

    fun clearLoginStatus() {
        _loginStatus.value = InicioSesionUiState.Loading // O un nuevo objeto Idle
    }

}

sealed interface InicioSesionUiState {
    object Success : InicioSesionUiState
    object Error : InicioSesionUiState
    object Loading : InicioSesionUiState
}