package com.example.clearcounts.ui.screens.InicioSesion

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.clearcounts.R
import com.example.clearcounts.data.local.repository.usuario.UserRepository

@HiltViewModel
class ViewModelInicioSesion @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginStatus = MutableStateFlow<InicioSesionUiState>(InicioSesionUiState.Idle)
    val loginStatus : StateFlow<InicioSesionUiState> = _loginStatus

    fun validateUser(email: String, password: String) {
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

    fun onFacebookLoginSucces(token: String) {
        viewModelScope.launch {
            _loginStatus.value = InicioSesionUiState.Loading
            val result = userRepository.signInFacebook(token)
            result.onSuccess {
                _loginStatus.value = InicioSesionUiState.Success
            }.onFailure {
                _loginStatus.value = InicioSesionUiState.Error
            }
        }
    }

    fun signInWithGoogle(activityContext: Context) {
        val credentialManager = CredentialManager.create(activityContext)
        viewModelScope.launch {
            _loginStatus.value = InicioSesionUiState.Loading
            try {
                val googleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(activityContext.getString(R.string.default_web_client_id))
                    .setAutoSelectEnabled(false)
                    .build()

                val passwordOption = GetPasswordOption()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .addCredentialOption(passwordOption)
                    .build()

                val result = credentialManager.getCredential(
                    request = request,
                    context = activityContext
                )

                handleSignInResult(result)

            } catch (e: GetCredentialCancellationException) {
                _loginStatus.value = InicioSesionUiState.Idle
            } catch (e: NoCredentialException) {
                _loginStatus.value = InicioSesionUiState.ErrorEspecifico("No se encontraron credenciales")
            } catch (e: GetCredentialException) {
                _loginStatus.value = InicioSesionUiState.ErrorEspecifico(e.message ?: "Error al obtener credenciales")
            } catch (e: Exception) {
                _loginStatus.value = InicioSesionUiState.ErrorEspecifico(e.message ?: "Error inesperado")
            }
        }
    }

    private suspend fun handleSignInResult(result: GetCredentialResponse) {
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        authenticateWithFirebase(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        _loginStatus.value = InicioSesionUiState.ErrorEspecifico("Error al procesar credencial de Google")
                    }
                } else {
                    _loginStatus.value = InicioSesionUiState.ErrorEspecifico("Tipo de credencial no válido")
                }
            }
            else -> {
                _loginStatus.value = InicioSesionUiState.ErrorEspecifico("Credencial no soportada")
            }
        }
    }

    private suspend fun authenticateWithFirebase(token: String) {
        val result = userRepository.signInGoogle(token)
        result.onSuccess {
            _loginStatus.value = InicioSesionUiState.Success
        }.onFailure { exception ->
            _loginStatus.value = InicioSesionUiState.ErrorEspecifico(
                exception.message ?: "Error al autenticar con Firebase"
            )
        }
    }

    fun clearLoginStatus() {
        _loginStatus.value = InicioSesionUiState.Idle
    }
}

sealed interface InicioSesionUiState {

    object Idle : InicioSesionUiState
    object Success : InicioSesionUiState
    object  Error : InicioSesionUiState
    object Loading : InicioSesionUiState

    data class ErrorEspecifico(val message: String?) : InicioSesionUiState
}