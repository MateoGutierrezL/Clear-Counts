package com.example.clearcounts.ui.screens.InicioSesion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.data.database.entities.UserEntity
import com.example.clearcounts.utils.AlertaCamposVacios
import at.favre.lib.crypto.bcrypt.BCrypt


@Composable
fun PantallaRegistro(
    navegarBotonRegistrarme:() -> Unit,
    textoNavegarInicioSesion:() -> Unit,
    viewModel: RegistroUsuarioViewModel = hiltViewModel()
) {

    // Variables para los campos de texto
    var nombreUsuario by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var correoUsuario by remember { mutableStateOf("") }

    val coloresOutlined = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
    )

    // Visibilidad de las alertas
    var showDialog by remember { mutableStateOf(false) }
    var showSuccesDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }

    //Instanciar la clase de LocalFocusManager
    val focusManager = LocalFocusManager.current

    val registerStatus by viewModel.registerStatus.collectAsState()

    LaunchedEffect(key1 = registerStatus) {
        when(registerStatus) {
            is RegistroUsuarioUiState.Success -> {

                showSuccesDialog = true

            }
            is RegistroUsuarioUiState.Error -> {

                showErrorDialog = true
            }
            is RegistroUsuarioUiState.Loading -> {
                // Opcional: mostrar un ProgressBar en la UI
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
            .pointerInput(Unit) { // Usar Unit para que se ejecute una sola vez, se implementa en el contenedor principal
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
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

            // CAMBIO: Color del texto usa onSurface para contraste
            Text(
                stringResource(R.string.registrar_nueva_cuenta),
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column {
                Text(
                    text = stringResource(R.string.nombre),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    // CAMBIO: Color del texto usa onSurface para contraste
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = nombreUsuario,
                    onValueChange = { nombreUsuario = it },
                    placeholder = { Text(stringResource(R.string.ingrese_su_nombre_completo), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            }

            Column {

                Text(
                    text = stringResource(R.string.numero_de_celular),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    // CAMBIO: Color del texto usa onSurface para contraste
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = numero,
                    onValueChange = { numero = it },
                    placeholder = { Text(stringResource(R.string.ingrese_su_n_mero), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )

            }

            Column {

                Text(
                    text = stringResource(R.string.correo),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    // CAMBIO: Color del texto usa onSurface para contraste
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = correoUsuario,
                    onValueChange = { correoUsuario = it },
                    placeholder = { Text(stringResource(R.string.ingrese_su_correo_electr_nico), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )
            }

            Column {

                Text(
                    text = stringResource(R.string.contrasena),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    // CAMBIO: Color del texto usa onSurface para contraste
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .align(Alignment.Start),
                )

                OutlinedTextField(
                    value = contrasena,
                    onValueChange = { contrasena = it },
                    placeholder = { Text(stringResource(R.string.ingrese_su_contrase_a), color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )

            }

            //Verficacion de que todo este debidamente diligenciado
            Button(
                onClick = {
                    if (nombreUsuario.isBlank() || numero.isBlank() || correoUsuario.isBlank() || contrasena.isBlank()) {
                        showDialog = true // Muestra la alerta
                    } else {
                        val contrasenaHasheada = BCrypt.withDefaults().hashToString(12, contrasena.toCharArray())
                        val newUser = UserEntity(
                            id = 0,
                            nombre = nombreUsuario,
                            numero = numero,
                            correo = correoUsuario,
                            contrasena = contrasenaHasheada //Hashear contraseña
                        )

                        // 2. Llamar al ViewModel
                        viewModel.signUp(newUser)

                    }
                },
                colors = ButtonDefaults.buttonColors(
                    // CAMBIO: Color del botón usa primary
                    containerColor = MaterialTheme.colorScheme.primary,
                    // Añadimos el color del texto para que contraste
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
                modifier = Modifier.clickable {
                    textoNavegarInicioSesion()
                }
            )

        }
    }

    // Llama a la Alerta solo si la variable de estado es true
    if (showDialog) {
        AlertaCamposVacios(onDismiss = { showDialog = false },
            titulo = stringResource(R.string.llenar_todos_los_campos) ,
            stringResource(R.string.llenar_campos)
        )
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
        RegisterError(
            onDismiss = {
                showErrorDialog = false
            }
        )
    }
}

@Composable
fun RegisterSucces(
    onDismiss: () -> Unit,
    title: String,
    text: String
){

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
                Text(
                    text = text,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.aceptar))
                }
            }
        }
    }
}

@Composable
fun RegisterError(onDismiss: () -> Unit){

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
                    tint = Color.Red, // Color rojo
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