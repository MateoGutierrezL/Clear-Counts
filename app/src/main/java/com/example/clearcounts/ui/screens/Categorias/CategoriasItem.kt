package com.example.clearcounts.ui.screens.Categorias

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clearcounts.data.database.entities.CategoryEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasItem(
    navController: NavController,
    categorias: CategoryEntity,
    sheetState: SheetState,
    ruta: String
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Convierte el String del icono al resource ID del drawable
    val iconoId = remember(categorias.icono) {
        context.resources.getIdentifier(categorias.icono, "drawable", context.packageName)
    }

    Button(
        onClick = {
            scope.launch {
                delay(500)
                sheetState.hide()
                navController.navigate("$ruta/${categorias.icono}/${categorias.nombre}")
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = Color.Black
        ),
        border = BorderStroke(0.3.dp,Color.Gray)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            if (iconoId != 0) { // 0 significa que no encontró el drawable
                Icon(
                    painter = painterResource(id = iconoId),
                    contentDescription = categorias.nombre,
                    modifier = Modifier.size(40.dp).padding(top = 7.dp),
                    tint = Color.Unspecified
                )
            }
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 11.dp, bottom = 11.dp),
                text = categorias.getNombreTraducido(),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )
        }
    }
    Spacer(modifier = Modifier.size(10.dp))
}