package com.example.clearcounts.ui.screens.Perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(

    private val userRepository: UserRepository
): ViewModel() {

    fun updateUser(userEntity: UserEntity){

        viewModelScope.launch {

            try {

                userRepository.updateUser(userEntity)

                println("Usuario actualizado con exito")

            }catch (e: Exception){

                println("Actualizacion de usuario denegada")
            }
        }
    }
}