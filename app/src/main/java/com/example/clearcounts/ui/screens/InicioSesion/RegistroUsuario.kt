package com.example.clearcounts.ui.screens.InicioSesion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.data.database.entities.UserEntity
import com.example.clearcounts.utils.AlertaCamposVacios


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

    // Visibilidad de la alerta
    var showDialog by remember { mutableStateOf(false) }

    // Variable para manejar los colores del outlinedText, viene de la carpeta utils
    // CAMBIO: Usamos colores dinámicos de MaterialTheme.colorScheme
    val coloresOutlined = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary, // Color principal del tema
        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), // Un color neutro sutil
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
    )

    //Instanciar la clase de LocalFocusManager
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            // CAMBIO: Aseguramos que el fondo se adapte al tema
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
                "Registrar nueva cuenta",
                fontSize = 25.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column {
                Text(
                    text = "Nombre",
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
                    placeholder = { Text("Ingrese su nombre completo", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )
            }

            Column {

                Text(
                    text = "Número de celular",
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
                    placeholder = { Text("Ingrese su número", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )

            }

            Column {

                Text(
                    text = "Correo",
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
                    placeholder = { Text("Ingrese su correo electrónico", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined
                )
            }

            Column {

                Text(
                    text = "Contraseña",
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
                    placeholder = { Text("Ingrese su contraseña", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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

                        val newUser = UserEntity(
                            id = 0,
                            nombre = nombreUsuario,
                            numero = numero,
                            correo = correoUsuario,
                            contrasena = contrasena //Hashear contraseña
                        )

                        // 2. Llamar al ViewModel
                        viewModel.insertUser(newUser)

                        navegarBotonRegistrarme()

                    }
                },
                colors = ButtonDefaults.buttonColors(
                    // CAMBIO: Color del botón usa primary
                    containerColor = MaterialTheme.colorScheme.primary,
                    // Añadimos el color del texto para que contraste
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Registrarme")
            }

            //Texto para la navegación hacia el inicio de sesión
            val annotatedString = buildAnnotatedString {
                // CAMBIO: El color por defecto del texto es onSurface
                withStyle(style = SpanStyle(MaterialTheme.colorScheme.onSurface)) {
                    append("¿Ya tienes una cuenta? ")
                }

                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        // CAMBIO: El color del enlace usa primary
                        color = MaterialTheme.colorScheme.primary
                    )
                ) {
                    append("Inicia sesión")
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
            titulo = "Llenar todos los campos" ,
            stringResource(R.string.llenar_campos)
        )
    }
}





