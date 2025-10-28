package com.example.clearcounts.ui.Barras

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.MaterialTheme
import com.example.clearcounts.ui.Barras.DrawerItem
import com.example.clearcounts.ui.theme.AzulBotones

@Composable
fun NavigationDrawer(
    name: String,
    email: String,
    items: List<DrawerItem>,
    modifier: Modifier = Modifier,
    onItemClick: (DrawerItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Encabezado del cajón de navegación
        Column(
            modifier = Modifier
                .fillMaxWidth().
                drawBehind {
                    // Obtenemos el tamaño del Composable
                    val strokeWidth = 2.dp.toPx()
                    val y = size.height - strokeWidth / 2 // Posición Y en la parte inferior

                    drawLine(
                        color = AzulBotones,
                        start = Offset(0f, y), // Empieza en la esquina inferior izquierda
                        end = Offset(size.width, y), // Termina en la esquina inferior derecha
                        strokeWidth = strokeWidth
                    )
                },
            verticalArrangement = Arrangement.Center,

            ) {
            Text(text = name, style = MaterialTheme.typography.titleLarge)
            Text(text = email, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))



        // Elementos del menú
        items.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(it) }
                    .padding(16.dp),
                verticalAlignment = CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = it.icon), // <-- Usar item.icon aquí
                    contentDescription = it.text,
                    modifier = Modifier.size(30.dp)// O el color que desees
                )
                Spacer(modifier = Modifier.width(25.dp))
                Text(text = it.text)
            }
            }
        }
}