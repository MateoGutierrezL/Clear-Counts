package com.example.clearcounts.ui.screens.InicioSesion

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.utils.AlertaCamposVacios
import com.example.clearcounts.utils.OutlinedTextFieldColors
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.utils.LoadingOverlay
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.delay


@Composable
fun PantallaInicioSesion(
    navegarRegistroUsuario: () -> Unit,
    navegarOlvidoContrasena: () -> Unit,
    navegarInicio: () -> Unit,
    viewModel: ViewModelInicioSesion = hiltViewModel()
) {
    val context = LocalContext.current
    var hayInternet by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        hayInternet = isInternetAvailable(context)
        while (true) {
            delay(3000)
            hayInternet = isInternetAvailable(context)
        }
    }

    val isLoadingGoogle by viewModel.isLoadingGoogle.collectAsState()
    val isLoadingFacebook by viewModel.isLoadingFacebook.collectAsState()
    val isLoadingEmail by viewModel.isLoadingEmail.collectAsState()
    val isLoadingAny = isLoadingGoogle || isLoadingFacebook || isLoadingEmail

    var contrasena by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var errorCorreo by remember { mutableStateOf("") }
    var errorContrasena by remember { mutableStateOf("") }
    var showLoginErrorDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val rojoPersonalizado = Color(0xFFC95050)
    val coloresOutlined = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        errorBorderColor = rojoPersonalizado,
        errorCursorColor = rojoPersonalizado,
        errorTextColor = MaterialTheme.colorScheme.onSurface,
        errorLabelColor = rojoPersonalizado,
        errorSupportingTextColor = rojoPersonalizado,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    val loginStatus by viewModel.loginStatus.collectAsState()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val loginLauncher = rememberLauncherForActivityResult(
        LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    ) {}

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    viewModel.onFacebookLoginSucces(result.accessToken.token)
                }
                override fun onCancel() {}
                override fun onError(error: FacebookException) {}
            }
        )
        onDispose { LoginManager.getInstance().unregisterCallback(callbackManager) }
    }

    when (loginStatus) {
        is InicioSesionUiState.Error -> {
            if (hayInternet) showLoginErrorDialog = true
        }
        is InicioSesionUiState.Success -> navegarInicio()
        else -> {}
    }

    // ✅ Box principal que contiene todo incluyendo el overlay
    Box(modifier = Modifier.fillMaxSize()) {

        // ── CONTENIDO DE LA PANTALLA ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.Center
        ) {
            // Banner sin internet
            AnimatedVisibility(
                visible = !hayInternet,
                modifier = Modifier.align(Alignment.TopCenter),
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE53935))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.sin_internet_mensaje),
                        color = Color.White,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_clear_counts),
                    contentDescription = "Logo de Clear Counts"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.ingrese_a_su_cuenta),
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                // ── CAMPO CORREO ──
                Column {
                    Text(
                        text = stringResource(R.string.correo),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .align(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = {
                            correoUsuario = it
                            if (it.isNotBlank()) errorCorreo = ""
                        },
                        placeholder = { Text(stringResource(R.string.ingrese_su_correo_electr_nico)) },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined,
                        isError = errorCorreo.isNotEmpty(),
                        enabled = !isLoadingAny
                    )
                    if (errorCorreo.isNotEmpty()) {
                        Text(
                            text = errorCorreo,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // ── CAMPO CONTRASEÑA ──
                Column {
                    Text(
                        text = stringResource(R.string.contrasena),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                            .align(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = {
                            contrasena = it
                            if (it.isNotBlank()) errorContrasena = ""
                        },
                        placeholder = { Text(stringResource(R.string.ingrese_su_contrase_a)) },
                        visualTransformation = if (mostrarContrasena) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined,
                        isError = errorContrasena.isNotEmpty(),
                        enabled = !isLoadingAny,
                        trailingIcon = {
                            IconButton(onClick = { mostrarContrasena = !mostrarContrasena }) {
                                Icon(
                                    imageVector = if (mostrarContrasena) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    if (errorContrasena.isNotEmpty()) {
                        Text(
                            text = errorContrasena,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // ── BOTÓN INGRESAR CON EMAIL ──
                Button(
                    onClick = {
                        errorCorreo = when {
                            correoUsuario.isBlank() -> context.getString(R.string.el_correo_es_requerido)
                            !correoUsuario.contains("@") -> context.getString(R.string.ingresa_un_correo_valido)
                            else -> ""
                        }
                        errorContrasena = if (contrasena.isBlank())
                            context.getString(R.string.la_contrasena_es_requerida) else ""

                        if (errorCorreo.isEmpty() && errorContrasena.isEmpty()) {
                            if (!hayInternet) {
                                Toast.makeText(context, R.string.sin_internet_mensaje, Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.validateUser(correoUsuario, contrasena)
                            }
                        }
                    },
                    enabled = !isLoadingAny,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, end = 48.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, gris),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    if (isLoadingEmail) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            stringResource(R.string.ingresar),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                // ── BOTONES FACEBOOK Y GOOGLE ──
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Facebook
                    Button(
                        onClick = {
                            loginLauncher.launch(listOf("email", "public_profile"))
                        },
                        enabled = !isLoadingAny,
                        modifier = Modifier.border(
                            border = BorderStroke(1.dp, color = Color(0xFF1877F2)),
                            shape = ButtonDefaults.shape
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        if (isLoadingFacebook) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color(0xFF1877F2),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.facebook_logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Facebook", color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }

                    // Google
                    Button(
                        onClick = { viewModel.signInWithGoogle(context) },
                        enabled = !isLoadingAny,
                        modifier = Modifier.border(
                            border = BorderStroke(1.dp, gris),
                            shape = ButtonDefaults.shape
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        if (isLoadingGoogle) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color(0xFF4285F4),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    painter = painterResource(id = R.drawable.google_logo),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Google", color = MaterialTheme.colorScheme.onBackground)
                            }
                        }
                    }
                }

                // ── LINKS OLVIDÉ CONTRASEÑA / REGISTRARSE ──
                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = stringResource(R.string.olvido_su_clave),
                        modifier = Modifier.clickable(enabled = !isLoadingAny) {
                            navegarOlvidoContrasena()
                        },
                        color = if (isLoadingAny)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(42.dp))
                    Text(
                        text = stringResource(R.string.no_tiene_cuenta_reg_strese_aqu),
                        modifier = Modifier.clickable(enabled = !isLoadingAny) {
                            navegarRegistroUsuario()
                        },
                        color = if (isLoadingAny)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // ── OVERLAY DE CARGA ──
        LoadingOverlay(
            visible = isLoadingGoogle,
            mensaje = stringResource(R.string.iniciando_con_google)
        )
        LoadingOverlay(
            visible = isLoadingFacebook,
            mensaje = stringResource(R.string.iniciando_con_facebook)
        )
        LoadingOverlay(
            visible = isLoadingEmail,
            mensaje = stringResource(R.string.iniciando_sesion)
        )
    }

    if (showLoginErrorDialog) {
        LoginError(
            onDismiss = {
                showLoginErrorDialog = false
                viewModel.clearLoginStatus()
            }
        )
    }
}
@Composable
fun LoginError(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Inicio sesion Fallido",
                    tint = Color.Red,
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.inicio_sesi_n_fallido),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.correo_electronico_o_contrase_a_no_validos),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text(stringResource(R.string.aceptar))
                }
            }
        }
    }
}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}