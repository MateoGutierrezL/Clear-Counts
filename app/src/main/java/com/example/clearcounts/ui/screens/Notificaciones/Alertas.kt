package com.example.clearcounts.ui.screens.Notificaciones

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R


data class alerta(
    @DrawableRes val icono: Int,
    @StringRes val titulo: Int,
    @StringRes val contenido: Int
)

object DataSource{
    val alertas = mutableStateListOf(
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas),
        alerta(R.drawable.alerta,R.string.titulo_alertas,R.string.contenido_alertas)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Alertas(alerta: alerta) {
    val tituloString = stringResource(id = alerta.titulo)
    val contenidoString = stringResource(id = alerta.contenido)
    Button(
        onClick = {

        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = alerta.icono),
                contentDescription = stringResource(id = alerta.titulo),
                modifier = Modifier.size(70.dp).padding(end = 4.dp,top = 10.dp),
                tint = Color.Unspecified
            )
            Column {
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 11.dp),
                    text = stringResource(id = alerta.titulo),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
                Text(
                    modifier = Modifier.padding(start = 8.dp, top = 11.dp),
                    text = stringResource(id = alerta.contenido),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 20.sp
                )
            }

        }
    }
    Spacer(
        modifier = Modifier.size(10.dp)
    )
    HorizontalDivider(
        modifier = Modifier,
        thickness = 3.dp,
        color = MaterialTheme.colorScheme.primary
    )
}





