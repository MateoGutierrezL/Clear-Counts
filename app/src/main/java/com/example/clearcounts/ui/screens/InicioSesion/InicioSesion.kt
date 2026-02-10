package com.example.clearcounts.ui.screens.InicioSesion

import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.ui.theme.gris
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


@Composable
fun PantallaInicioSesion(
    navegarRegistroUsuario:() -> Unit,
    navegarOlvidoContrasena:() -> Unit,
    navegarInicio:() -> Unit,
    viewModel: ViewModelInicioSesion = hiltViewModel()
) {


    //Variables que almacenan los datos que son proporcionados en cada uno de los textfield
    var contrasena by remember { mutableStateOf("") }

    var correoUsuario by remember { mutableStateOf("") }

    //Variable para manejar la alerta de campos vacios
    var showDialog by remember { mutableStateOf(false) }

    var showLoginErrorDialog by remember { mutableStateOf(false)}

    //Instanciar la clase de LocalFocusManager
    val focusManager = LocalFocusManager.current

    //Variable para manejar los colores del outlinedText, viene de la carpeta utils
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )

    val loginStatus by viewModel.loginStatus.collectAsState()

    val context = LocalContext.current
    val callbackManager = remember { CallbackManager.Factory.create() }

    // Configuramos el Launcher para recibir el resultado de Facebook
    val loginLauncher = rememberLauncherForActivityResult(
        LoginManager.getInstance().createLogInActivityResultContract(callbackManager)
    ) {
        // El resultado se maneja a través del callbackManager
    }

    // Registramos el callback
    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    viewModel.onFacebookLoginSucces(result.accessToken.token)
                }
                override fun onCancel() { /* Manejar cancelación */ }
                override fun onError(error: FacebookException) {
                    // Aquí podrías disparar el estado de error en tu ViewModel
                }
            }
        )
        onDispose { LoginManager.getInstance().unregisterCallback(callbackManager) }
    }


    when(loginStatus){

        is InicioSesionUiState.Error -> showLoginErrorDialog = true
        is InicioSesionUiState.Loading -> LoadingScreen()
        is InicioSesionUiState.Success -> navegarInicio()
        else -> "Error"

    }

        //Contenedor box que abarca toda la pantalla del celular y centra el contenido
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
                .pointerInput(Unit) {// Usar Unit para que se ejecute una sola vez, se implementa en el contenedor principal

                    detectTapGestures(onTap = {
                        // Al detectar un toque (tap), limpiar el foco
                        focusManager.clearFocus()
                    })
                }
            ,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),

            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_clear_counts),
                    contentDescription = "Logo de Clear Counts"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ingrese a su cuenta",
                    fontSize = 25.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Column {
                    Text(
                        text = "Correo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                            .align(Alignment.Start)
                    )

                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = { correoUsuario = it },
                        placeholder = { Text("Ingrese su correo electrónico") },
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
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .padding(bottom = 2.dp)
                            .align(Alignment.Start)
                    )

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        placeholder = { Text("Ingrese su contraseña") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined
                    )
                }

                //Verficacion de que este debidamente diligenciado
                Button(
                    onClick = {
                        if (correoUsuario.isBlank() || contrasena.isBlank()) {
                            showDialog = true
                        }
                        else {
                            viewModel.validateUser(correoUsuario, contrasena)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, end = 48.dp),
                    shape = RoundedCornerShape(16.dp) ,
                    border = BorderStroke(1.dp, gris ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text("Ingresar", color = MaterialTheme.colorScheme.onBackground)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){

                    Button(
                        onClick = {
                            loginLauncher.launch(listOf("email", "public_profile"))
                        },
                        modifier = Modifier.border(
                            border = BorderStroke(1.dp, color = Color(0xFF1877F2)),
                            shape = ButtonDefaults.shape,

                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {

                        Row (verticalAlignment = Alignment.CenterVertically){

                            Icon(

                                painter = painterResource(id = R.drawable.facebook_logo),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Unspecified

                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Facebook",
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            viewModel.signInWithGoogle(context)
                        },
                        modifier = Modifier.border(
                            border = BorderStroke(1.dp, gris),
                            shape = ButtonDefaults.shape
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {

                        Row (verticalAlignment = Alignment.CenterVertically){

                            Icon(

                                painter = painterResource(id = R.drawable.google_logo),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.Unspecified

                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Google",
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        }
                    }
                }

                Row (horizontalArrangement = Arrangement.Center){
                    //Texto para navegar hacia la pantalla de olvido su contraseña
                    Text(
                        text = "¿Olvidó su clave?",
                        modifier = Modifier
                        .clickable {

                            navegarOlvidoContrasena()
                        },
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(42.dp))

                    Text(
                        text = "¿No tiene cuenta?\n Regístrese aquí",
                        modifier = Modifier
                            .clickable {

                                navegarRegistroUsuario()
                            },
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }
        // Llama a la Alerta
        if (showDialog) {
            AlertaCamposVacios(
                onDismiss = { showDialog = false },
                titulo = "Llenar todos los campos",
                mensaje = stringResource(R.string.llenar_campos)
            )
        }

    if (showLoginErrorDialog){
        LoginError(
            onDismiss = {
                showLoginErrorDialog = false

                viewModel.clearLoginStatus()
            }
        )
    }
}

@Composable
fun LoginError(onDismiss: () -> Unit){

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
                    tint = Color.Red, // Color rojo
                    modifier = Modifier.size(60.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "¡Inicio sesión Fallido!",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))


                Text(
                    text = "Correo electronico o contraseña no validos",
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
                    Text("Aceptar")
                }
            }
        }
    }
}

@Composable
fun LoadingScreen(){
    CircularProgressIndicator(
        modifier = Modifier.size(64.dp),
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}