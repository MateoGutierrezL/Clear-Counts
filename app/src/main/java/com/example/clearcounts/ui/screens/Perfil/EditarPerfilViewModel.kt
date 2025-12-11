package com.example.clearcounts.ui.screens.Perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(

    private val userRepository: UserRepository
): ViewModel() {

    private val currentUserFlow: Flow<UserEntity?> = userRepository.getCurrentLoggedInUser()

    private var currentUserId: Int? = null
    private var currentPassword = ""

    init {
        viewModelScope.launch {
            currentUserFlow.firstOrNull()?.let { user ->
                currentUserId = user.id
                currentPassword = user.contrasena
            }
        }
    }

    fun updateUser(
        nombre: String,
        numero: String,
        correo: String
    ){

        viewModelScope.launch {

            val idToUpdate = currentUserId

            if (idToUpdate == null) {
                println("Error: No se pudo determinar el ID del usuario logueado.")
                return@launch
            }

            val user = UserEntity(
                id = idToUpdate,
                nombre = nombre,
                numero = numero,
                correo = correo,
                contrasena = currentPassword
            )

            userRepository.updateUser(user)

            try {
                println("Usuario actualizado con exito")

            }catch (e: Exception){

                println("Actualizacion de usuario denegada")
            }
        }
    }
}