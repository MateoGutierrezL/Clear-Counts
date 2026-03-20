package com.example.clearcounts.ui.screens.InicioSesion

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.utils.AlertaCamposVacios
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.clearcounts.data.local.database.entities.UserEntity
import kotlinx.coroutines.delay


@Composable
fun PantallaRegistro(
    navegarBotonRegistrarme: () -> Unit,
    textoNavegarInicioSesion: () -> Unit,
    viewModel: RegistroUsuarioViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hayInternet by remember { mutableStateOf(isInternetAvailable(context)) }

// Verifica cada vez que la pantalla es visible
    LaunchedEffect(Unit) {
        while (true) {
            hayInternet = isInternetAvailable(context)
            delay(3000) // Verifica cada 3 segundos
        }
    }
    var nombreUsuario by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }
    var mostrarContrasena by remember { mutableStateOf(false) }
    var errorNombre by remember { mutableStateOf("") }
    var errorNumero by remember { mutableStateOf("") }
    var errorCorreo by remember { mutableStateOf("") }


    var showSuccesDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val registerStatus by viewModel.registerStatus.collectAsState()
    val rojoPersonalizado = Color(0xFFC95050)
    val coloresOutlined = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        errorBorderColor = rojoPersonalizado,        //  Borde en error
        errorCursorColor = rojoPersonalizado,        //  Cursor en error
        errorTextColor = MaterialTheme.colorScheme.onSurface,
        errorLabelColor = rojoPersonalizado,         //  Label en error
        errorSupportingTextColor = rojoPersonalizado, //  Texto de soporte en error
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )

    val coloresOutlinedcontrasena = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        errorBorderColor = rojoPersonalizado,
        errorCursorColor = rojoPersonalizado,
        errorTextColor = rojoPersonalizado,
        errorLabelColor = rojoPersonalizado,
        errorSupportingTextColor = rojoPersonalizado,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
    )

    fun validar(): Boolean {
        errorNombre = if (nombreUsuario.isBlank()) context.getString(R.string.el_nombre_es_requerido) else ""
        errorNumero = when {
            numero.isBlank() -> context.getString(R.string.el_numero_es_requerido)
            numero.length < 7 -> context.getString(R.string.ingresa_un_numero_valido)
            else -> ""
        }
        errorCorreo = when {
            correoUsuario.isBlank() -> context.getString(R.string.el_correo_es_requerido)
            !correoUsuario.contains("@") -> context.getString(R.string.ingresa_un_correo_valido)
            else -> ""
        }
        return errorNombre.isEmpty() && errorNumero.isEmpty() &&
                errorCorreo.isEmpty()
    }

    LaunchedEffect(key1 = registerStatus) {
        when (registerStatus) {
            is RegistroUsuarioUiState.Success -> showSuccesDialog = true
            is RegistroUsuarioUiState.Error -> showErrorDialog = true
            else -> {}
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {


        // Banner sin internet
        AnimatedVisibility(
            visible = !hayInternet,
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .verticalScroll(rememberScrollState())
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_clear_counts),
                    contentDescription = "Logo de Clear Counts"
                )

                Text(
                    stringResource(R.string.registrar_nueva_cuenta),
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Campo Nombre
                Column {
                    Text(
                        text = stringResource(R.string.nombre),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .align(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = nombreUsuario,
                        onValueChange = {
                            nombreUsuario = it
                            if (it.isNotBlank()) errorNombre = ""
                        },
                        placeholder = {
                            Text(
                                stringResource(R.string.ingrese_su_nombre_completo),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined,
                        isError = errorNombre.isNotEmpty(),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                    )
                    if (errorNombre.isNotEmpty()) {
                        Text(
                            text = errorNombre,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // Campo Número
                Column {
                    Text(
                        text = stringResource(R.string.numero_de_celular),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .align(Alignment.Start)
                    )
                    OutlinedTextField(
                        value = numero,
                        onValueChange = {
                            numero = it
                            if (it.isNotBlank()) errorNumero = ""
                        },
                        placeholder = {
                            Text(
                                stringResource(R.string.ingrese_su_n_mero),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined,
                        isError = errorNumero.isNotEmpty()
                    )
                    if (errorNumero.isNotEmpty()) {
                        Text(
                            text = errorNumero,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // Campo Correo
                Column {
                    Text(
                        text = stringResource(R.string.correo),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
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
                        placeholder = {
                            Text(
                                stringResource(R.string.ingrese_su_correo_electr_nico),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined,
                        isError = errorCorreo.isNotEmpty()
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

                // Campo Contraseña
                val esError = contrasena.isNotEmpty() && contrasena.length < 6

                Column {
                    Text(
                        text = stringResource(R.string.contrasena),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .align(Alignment.Start),
                    )

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        placeholder = {
                            Text(
                                stringResource(R.string.ingrese_su_contrase_a),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        visualTransformation = if (mostrarContrasena) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        isError = esError,
                        colors = coloresOutlinedcontrasena,
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


                    if (esError) {
                        Text(
                            text = stringResource(R.string.contrasena_minimo_6),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                        )
                    }
                }

                // Botón Registrarme
                Button(
                    onClick = {
                        if (validar()) {
                            val contrasenaHasheada = BCrypt.withDefaults()
                                .hashToString(12, contrasena.toCharArray())
                            val newUser = UserEntity(
                                id = 0,
                                nombre = nombreUsuario,
                                numero = numero,
                                correo = correoUsuario,
                                contrasena = contrasenaHasheada
                            )
                            viewModel.signUp(newUser, contrasena)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(stringResource(R.string.registrarme))
                }

                val annotatedString = buildAnnotatedString {
                    withStyle(style = SpanStyle(MaterialTheme.colorScheme.onSurface)) {
                        append(stringResource(R.string.ya_tienes_una_cuenta))
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(stringResource(R.string.inicia_sesion))
                    }
                }

                Text(
                    text = annotatedString,
                    modifier = Modifier.clickable { textoNavegarInicioSesion() }
                )
            }
        }
    }

    @Composable
    fun RegisterSucces(
        onDismiss: () -> Unit,
        title: String,
        text: String
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = text, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onDismiss) {
                        Text(stringResource(R.string.aceptar))
                    }
                }
            }
        }
    }

    @Composable
    fun RegisterError(onDismiss: () -> Unit) {
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
                        contentDescription = stringResource(R.string.registro_fallido),
                        tint = Color.Red,
                        modifier = Modifier.size(60.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.registro_fallido),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.ocurri_un_error_inesperado_por_favor_intenta_de_nuevo),
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

    if (showSuccesDialog) {
        RegisterSucces(
            onDismiss = {
                showSuccesDialog = false
                navegarBotonRegistrarme()
            },
            title = stringResource(R.string.registro_exitoso),
            text = stringResource(R.string.tus_datos_han_sido_guardados_correctamente)
        )
    }

    if (showErrorDialog) {
        RegisterError(onDismiss = { showErrorDialog = false })
    }


}


