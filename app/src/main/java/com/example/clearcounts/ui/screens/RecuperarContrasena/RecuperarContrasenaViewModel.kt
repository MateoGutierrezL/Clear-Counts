package com.example.clearcounts.ui.screens.RecuperarContrasena

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clearcounts.data.local.repository.usuario.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class RecuperarContrasenaViewModel @Inject constructor(
    private val userRepository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _proveedorSocial = MutableStateFlow<String?>(null)
    val proveedorSocial: StateFlow<String?> = _proveedorSocial.asStateFlow()
    private val _correoEnviado = MutableStateFlow("")
    val correoEnviado: StateFlow<String> = _correoEnviado.asStateFlow()
    private val _puedeNavegar = MutableStateFlow(false)
    val puedeNavegar: StateFlow<Boolean> = _puedeNavegar.asStateFlow()

    fun limpiarEstado() {
        _proveedorSocial.value = null
        _puedeNavegar.value = false
    }

    fun sendEmailResetPassword(email: String) {
        viewModelScope.launch {
            try {
                userRepository.SendResetPassword(email)
                Toast.makeText(
                    context,
                    "Correo enviado para recuperar contraseña",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Error al enviar el correo",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun verificarProveedorYEnviar(correo: String) {
        viewModelScope.launch {
            try {

                val usuario = userRepository.getUserByEmail(correo)

                when {
                    usuario == null -> {
                        // Correo no registrado — enviamos igual y dejamos que Firebase maneje el error
                        sendEmailResetPassword(correo)
                        _puedeNavegar.value = true
                    }
                    usuario.proveedor == "google" -> {
                        _proveedorSocial.value = "google"
                        _puedeNavegar.value = false
                    }
                    usuario.proveedor == "facebook" -> {
                        _proveedorSocial.value = "facebook"
                        _puedeNavegar.value = false
                    }
                    else -> {
                        sendEmailResetPassword(correo)
                        _puedeNavegar.value = true
                    }
                }
            } catch (e: Exception) {
                Log.e("RecuperarContrasena", "Error: ${e.message}")
                sendEmailResetPassword(correo)
                _correoEnviado.value = correo
                _puedeNavegar.value = true
            }
        }
    }
}