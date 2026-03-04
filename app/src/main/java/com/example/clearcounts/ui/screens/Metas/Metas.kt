package com.example.clearcounts.ui.screens.Metas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.FloatingWindow


@Composable
fun Metas(
    onBotonCrear: (String) -> Unit
) {

    var currentTab by remember { mutableStateOf("Metas")}

    Scaffold(
        floatingActionButton = {
            BotonCrear(
                onBotonCrear = {
                    onBotonCrear(
                        currentTab
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(8.dp)
        ) {
            item {
                Encabezados()
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                RowEncabezado(
                    onTabSelected = { currentTab = it }
                )
            }
        }
    }

}

@Composable
fun Encabezados(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ItemEncabezado(
            icono = Icons.Default.TrackChanges,
            titulo = "Metas",
            contenido = "3",
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )

        ItemEncabezado(
            icono = Icons.Default.ErrorOutline,
            titulo = "Deudas",
            contenido = "10.200",
            tint = Color(0xFFE74C3C),
            modifier = Modifier.weight(1f)
        )

        ItemEncabezado(
            icono = Icons.Default.VerifiedUser,
            titulo = "Te deben",
            contenido = "1450",
            tint = Color(0xFF2ECC71),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ItemEncabezado(
    modifier: Modifier = Modifier,
    icono: ImageVector,
    titulo: String,
    contenido: String,
    tint: Color
){
    Column(
        modifier = modifier
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(imageVector = icono, contentDescription = null, modifier = Modifier.size(20.dp), tint = tint)

        Text(text = titulo, style = MaterialTheme.typography.bodyMedium)

        Text(
            text = contenido,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowEncabezado(
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Metas", "Deudas", "Préstamos")
    var selectedTab by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(50.dp))
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTab == index

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                        )
                        .clickable {
                            selectedTab = index
                            onTabSelected(title)
                        }
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else Color.Gray,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun BotonCrear(
    onBotonCrear:() -> Unit
){

    FloatingActionButton(
        onClick = {
            onBotonCrear()
                  },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        shape = CircleShape,
        modifier = Modifier.padding(start = 24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Añadir",
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}