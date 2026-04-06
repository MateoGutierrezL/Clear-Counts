package com.example.clearcounts.ui.screens.Barras

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.MaterialTheme
import com.example.clearcounts.ui.screens.Perfil.PerfilViewModel
import com.example.clearcounts.ui.theme.AzulBotones

@Composable
fun NavigationDrawer(
    profilePicture: Painter,
    name: String,
    email: String,
    items: List<DrawerItem>,
    modifier: Modifier = Modifier,
    viewModel: PerfilViewModel = hiltViewModel(),
    onItemClick: (DrawerItem) -> Unit
) {
    val context = LocalContext.current
    val currentUser by viewModel.currentUser.collectAsState()
    val esCorreoReal = currentUser?.correo?.contains("@") == true
    val avatarActual = currentUser?.avatar ?: "cacatuaninfa"
    val avatarId = remember(avatarActual) {
        context.resources.getIdentifier(avatarActual, "drawable", context.packageName)
    }
    Column(modifier = Modifier.fillMaxWidth()
        .drawBehind {
        // Obtenemos el tamaño del Composable
        val strokeWidth = 2.dp.toPx()
        val y = size.height - strokeWidth / 2

        drawLine(
            color = AzulBotones,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
    }) {
        // Encabezado del cajón de navegación
        Column(
            modifier = Modifier
                .fillMaxWidth().drawBehind {
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
            Row {
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp, start = 11.dp)
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                        .border(3.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarId != 0) {
                        Image(
                            painter = painterResource(id = avatarId),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = Color.White
                        )
                    }
                }
                Column {
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = name, style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 15.dp, top = 10.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = if (esCorreoReal) currentUser?.correo ?: "-" else "",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 15.dp, bottom = 5.dp)
                    )
                }
            }


            Spacer(modifier = Modifier.height(16.dp))
        }


        Spacer(modifier = Modifier.height(16.dp))

        //Elementos principales del menu
        val mainItems = items.filter { it != DrawerItem.LOG_OUT }

        // Elementos del menú
        mainItems.forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(it) }
                    .padding(16.dp),
                verticalAlignment = CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = it.icon), // Usar item.icon aquí
                    contentDescription = stringResource(it.text),
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(25.dp))
                Text(text = stringResource(it.text))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }

    Column {

        Spacer(modifier = Modifier.height(16.dp))

        Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick(DrawerItem.LOG_OUT) }
            .padding(16.dp),
        verticalAlignment = CenterVertically
    ) {
        Icon(
            painter = painterResource(id = DrawerItem.LOG_OUT.icon), // <-- Usar item.icon aquí
            contentDescription = stringResource(DrawerItem.LOG_OUT.text),
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(25.dp))
        Text(text = stringResource(DrawerItem.LOG_OUT.text))
    } }

}