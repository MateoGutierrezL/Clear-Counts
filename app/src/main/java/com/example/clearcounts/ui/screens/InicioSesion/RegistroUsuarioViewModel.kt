package com.example.clearcounts.ui.screens.InicioSesion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegistroUsuarioViewModel @Inject constructor(

    private val userRepository: UserRepository
): ViewModel() {

    fun insertUser(userEntity: UserEntity){

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