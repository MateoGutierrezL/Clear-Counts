package com.example.clearcounts.ui.screens.Perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.example.clearcounts.data.database.entities.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(

    private val userRepository: UserRepository
): ViewModel() {

    fun updateUser(
        user_id: Int,
        nombre: String,
        celular: String,
        correo: String
    ){

        viewModelScope.launch {

            try {

                val usuario: UserEntity? = userRepository.getUserByIdStream(user_id).firstOrNull()

                if (usuario != null){

                    /*
                    userRepository.updateUser(
                        userEntity(
                            id = usuario.id,
                            nombre = nombre,
                            numero = celular,
                            correo = correo,
                            contrasena = usuario.contrasena
                        )
                    )

                     */
                }

                userRepository.updateUser(UserEntity(
                    usuario?.id ?: user_id, nombre, celular, correo,
                    usuario?.contrasena ?: "nada"
                ))

                println("Usuario actualizado con exito")

            }catch (e: Exception){

                println("Actualizacion de usuario denegada")
            }
        }
    }
}