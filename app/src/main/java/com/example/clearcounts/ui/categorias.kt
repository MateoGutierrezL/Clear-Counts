package com.example.clearcounts.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.clearcounts.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class categorias(
    @DrawableRes val icono: Int,
    @StringRes val nombre: Int
)
object DataSource{
    val categoriasIngresos = mutableStateListOf(
        categorias(R.drawable.comida,R.string.categoria_comida),
        categorias(R.drawable.gasolina,R.string.categoria_gasolina),
        categorias(R.drawable.comida,R.string.categoria_comida),
        categorias(R.drawable.gasolina,R.string.categoria_gasolina),
    )
    val categoriasGastos = mutableStateListOf(
        categorias(R.drawable.comida,R.string.categoria_comida),
        categorias(R.drawable.gasolina,R.string.categoria_gasolina),
        categorias(R.drawable.comida,R.string.categoria_comida),
        categorias(R.drawable.gasolina,R.string.categoria_gasolina),

    )
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaItemIngresos(navController: NavController, categoria: categorias, sheetState: SheetState) {
    val scope = rememberCoroutineScope()
    val nombreString = stringResource(id = categoria.nombre)
    Button(
        onClick = {
            scope.launch {
                delay(500)
                sheetState.hide()
                navController.navigate("ingresos/${categoria.icono}/$nombreString")
            }
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
                painter = painterResource(id = categoria.icono),
                contentDescription = stringResource(id = categoria.nombre),
                modifier = Modifier.size(50.dp),
                tint = Color.Unspecified
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 11.dp),
                text = stringResource(id = categoria.nombre),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
        }
    }
    Spacer(
        modifier = Modifier.size(10.dp)
    )
    Text(
        text = "----------------------------------------------------------------------",
        color = Color.Cyan
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriaItemGastos(navController: NavController, categoria: categorias, sheetState: SheetState) {
    val scope = rememberCoroutineScope()
    val nombreString = stringResource(id = categoria.nombre)
    Button(
        onClick = {
            scope.launch {
                delay(500)
                sheetState.hide()
                navController.navigate("ingresos/${categoria.icono}/$nombreString")
            }
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
                painter = painterResource(id = categoria.icono),
                contentDescription = stringResource(id = categoria.nombre),
                modifier = Modifier.size(50.dp),
                tint = Color.Unspecified
            )
            Text(
                modifier = Modifier.padding(start = 8.dp, top = 11.dp),
                text = stringResource(id = categoria.nombre),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp
            )
        }
    }
    Spacer(
        modifier = Modifier.size(10.dp)
    )
    Text(
        text = "----------------------------------------------------------------------",
        color = Color.Cyan
    )
}