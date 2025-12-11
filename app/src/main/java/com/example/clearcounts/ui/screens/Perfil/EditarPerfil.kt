package com.example.clearcounts.ui.screens.Perfil

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.Icon
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.MainViewModel
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.ClearCountTheme
import com.example.clearcounts.utils.OutlinedTextFieldColors

@Composable
fun EditarPerfil(
    botonVolver: () -> Unit,
    viewModel: EditarPerfilViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
){

    val currentUser by mainViewModel.currentUser.collectAsStateWithLifecycle()

    var nombreUsuario by remember { mutableStateOf(currentUser?.nombre ?: "") }
    var correoUsuario by remember { mutableStateOf(currentUser?.correo ?: "") }
    var telefono by remember { mutableStateOf(currentUser?.numero ?: "") }

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            nombreUsuario = currentUser!!.nombre
            correoUsuario = currentUser!!.correo
            telefono = currentUser!!.numero
        }
    }

    //Variable para manejar los colores del outlinedText, viene de la carpeta utils
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )

    //Instanciar la clase de LocalFocusManager
    val focusManager = LocalFocusManager.current

    ClearCountTheme {

        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background).
            pointerInput(Unit) { // Usar Unit para que se ejecute una sola vez, se implementa en el contenedor principal
                detectTapGestures(onTap = {

                    focusManager.clearFocus()
                })
            },
            contentAlignment = Alignment.Center,

            )
        {
            IconButton(
                onClick = {
                    botonVolver()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 10.dp, start = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(30.dp)
                )
            }

            Column(modifier = Modifier.fillMaxSize()
                .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(
                    text = "Mi perfil",
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 15.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 80.dp)
                        .padding(bottom = 10.dp),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Image(painter = painterResource(id = R.drawable.user),
                    contentDescription = "Icono de usuario",
                    modifier = Modifier.size(120.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )

                Column {
                    Text(
                        text = "Nombre",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 6.dp)
                            .align(Alignment.Start),
                    )

                    OutlinedTextField(value = nombreUsuario,
                        onValueChange = { nombreUsuario = it },
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
                        text = "Número de celular",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 6.dp)
                            .align(Alignment.Start),
                    )

                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
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
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(bottom = 6.dp)
                            .align(Alignment.Start),
                    )

                    OutlinedTextField(
                        value = correoUsuario,
                        onValueChange = { correoUsuario = it },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = coloresOutlined
                    )
                }

                Button(onClick = {
                    viewModel.updateUser(
                        nombre = nombreUsuario,
                        numero = telefono,
                        correo = correoUsuario
                    )
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary)
                )
                {
                    Text("Guardar",
                        color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}