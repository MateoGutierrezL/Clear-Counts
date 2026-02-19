package com.example.clearcounts.ui.screens.RecuperarContrasena

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.UserRepository
import com.facebook.share.Sharer
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecuperarContrasenaViewModel @Inject constructor(

    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    fun sendEmailResetPassword(email: String){
        viewModelScope.launch {
            try {
                userRepository.SendResetPassword(email)
                Toast.makeText(context, "Correo enviado para recuperar contraseña", Toast.LENGTH_SHORT)
            }catch (e: Exception){
                Toast.makeText(context, "Error al enviar el correo", Toast.LENGTH_SHORT)
            }
        }
    }
}