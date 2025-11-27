package com.example.clearcounts.ui.screens.PreguntasComentarios

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulBotones
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.blanco
import com.example.clearcounts.ui.theme.fondo
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.ui.theme.negro
import com.example.clearcounts.utils.OutlinedTextFieldColors

@Preview(showBackground = true)
@Composable
fun PreguntasComentarios(){

    var comentario by remember { mutableStateOf("") }

    //Colores para el campo de texto
    val coloresOutlined = OutlinedTextFieldColors(
        focusedBorder = AzulEncabezado,
        unfocusedBorder = AzulEncabezado
    )

    //Instanciar la clase de LocalFocusManager
    val focusManager = LocalFocusManager.current


    Box(modifier = Modifier
        .fillMaxSize().
        pointerInput(Unit) { // Usar Unit para que se ejecute una sola vez, se implementa en el contenedor principal
            detectTapGestures(onTap = {

                focusManager.clearFocus()
            })
        }
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally) {


            Spacer(modifier = Modifier.height(80.dp))

            Text(text = stringResource(R.string.Texto_comentarios),
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 15.dp, start = 30.dp, end = 30.dp),
                textAlign = TextAlign.Center)



                OutlinedTextField(value = comentario,
                    onValueChange = { comentario = it },
                    placeholder = {Text("Escribe un comentario...")},
                    shape = RoundedCornerShape(16.dp),
                    colors = coloresOutlined,
                    maxLines = 15,
                    minLines = 10
                )



            Spacer(modifier = Modifier.height(20.dp))

                BotonesContacto()

        }
    }
}

@Composable
fun BotonesContacto(){

    Row {

        Button(
            onClick = {""},
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulBotones
            ),
            modifier = Modifier.height(46.dp)
        ) {

            Row (verticalAlignment = Alignment.CenterVertically){

                Icon(

                    painter = painterResource(id = R.drawable.enviar_mensaje),
                    contentDescription = "Icono de Enviar mensaje",
                    modifier = Modifier.size(20.dp),
                    tint = blanco

                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Enviar mensaje")

            }
        }

        Button(
            onClick = {""},
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

                    painter = painterResource(id = R.drawable.correo_electronico),
                    contentDescription = "Icono de Enviar correo",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.onBackground

                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Enviar correo",
                    color = MaterialTheme.colorScheme.onBackground
                )

            }
        }
    }
}

