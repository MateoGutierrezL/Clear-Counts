package com.example.clearcounts.ui.screens.Metas

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clearcounts.R
import com.example.clearcounts.data.database.entities.BudgetEntity


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Metas(
    onBotonCrear: (String) -> Unit,
    viewModel: MetasViewModel = hiltViewModel(),
    onVerDetalle: (BudgetEntity) -> Unit,
) {
    var currentTab by remember { mutableStateOf("Metas") }

    val metas by viewModel.metas.collectAsStateWithLifecycle()
    val deudas by viewModel.deudas.collectAsStateWithLifecycle()
    val prestamos by viewModel.prestamos.collectAsStateWithLifecycle()

    val cantidadMetas by viewModel.cantidadMetas.collectAsState()
    val totalDeudas by viewModel.totalDeudas.collectAsState()
    val totalTeDeben by viewModel.totalTeDeben.collectAsState()

    val (listaActual, iconoItem, tintItem, labelResumen, labelVacio) = when (currentTab) {
        "Metas" -> MetasTabConfig(
            lista = metas,
            icono = Icons.Default.TrackChanges,
            tint = MaterialTheme.colorScheme.primaryContainer,
            labelResumen = stringResource(R.string.metas_completadas),
            labelVacio = stringResource(R.string.no_hay_metas)
        )
        "Deudas" -> MetasTabConfig(
            lista = deudas,
            icono = Icons.Default.ErrorOutline,
            tint = Color(0xFFE74C3C),
            labelResumen = stringResource(R.string.deudas_pagadas),
            labelVacio = stringResource(R.string.no_hay_deudas)
        )
        else -> MetasTabConfig(
            lista = prestamos,
            icono = Icons.Default.VerifiedUser,
            tint = Color(0xFF2ECC71),
            labelResumen = stringResource(R.string.prestamos_pagados),
            labelVacio = stringResource(R.string.no_hay_prestamos)
        )
    }

    val totalLeft = listaActual.sumOf { it.cantidadRequerida - it.cantidadAcumulada }
    val completados = listaActual.count { it.cantidadAcumulada >= it.cantidadRequerida }

    Scaffold(
        floatingActionButton = {
            BotonCrear(onBotonCrear = { onBotonCrear(currentTab) })
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            item {
                Encabezados(
                    cantidadMetas = cantidadMetas,
                    totalDeudas = totalDeudas,
                    totalTeDeben = totalTeDeben
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                RowEncabezado(onTabSelected = { currentTab = it })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                ResumenCards(
                    totalLeft = totalLeft,
                    completados = completados,
                    total = listaActual.size,
                    labelCompletados = labelResumen
                )
            }

            if (listaActual.isEmpty()) {
                item {
                    EstadoVacio(
                        mensaje = labelVacio,
                        onCrearClick = { onBotonCrear(currentTab) }
                    )
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.actual),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Row(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "-$ ${totalLeft.formatMonto()}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }

                items(listaActual) { item ->
                    ItemBudgetCard(
                        item = item,
                        icono = iconoItem,
                        iconTint = tintItem,
                        onClick = { onVerDetalle(item) }
                    )
                }
            }
        }
    }
}

data class MetasTabConfig(
    val lista: List<BudgetEntity>,
    val icono: ImageVector,
    val tint: Color,
    val labelResumen: String,
    val labelVacio: String
)

@Composable
fun Encabezados(
    cantidadMetas: Int,
    totalDeudas: Double,
    totalTeDeben: Double
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ItemEncabezado(
            icono = Icons.Default.TrackChanges,
            titulo = stringResource(R.string.metas),
            contenido = cantidadMetas.toString(),
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )

        ItemEncabezado(
            icono = Icons.Default.ErrorOutline,
            titulo = stringResource(R.string.deudas),
            contenido = totalDeudas.formatMonto(),
            tint = Color(0xFFE74C3C),
            modifier = Modifier.weight(1f)
        )

        ItemEncabezado(
            icono = Icons.Default.VerifiedUser,
            titulo = stringResource(R.string.te_deben),
            contenido = totalTeDeben.formatMonto(),
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
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
            )
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
    var selectedTab by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                RoundedCornerShape(50.dp)
            )
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
            contentDescription = stringResource(R.string.añadir),
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ItemBudgetCard(
    item: BudgetEntity,
    icono: ImageVector,
    iconTint: Color = Color(0xFFE74C3C),
    onClick: () -> Unit
) {
    val progreso = if (item.cantidadRequerida > 0)
        (item.cantidadAcumulada / item.cantidadRequerida).toFloat().coerceIn(0f, 1f)
    else 0f

    val restante = item.cantidadRequerida - item.cantidadAcumulada

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                MaterialTheme.colorScheme.surfaceContainerHigh,
                                RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icono,
                            contentDescription = null,
                            tint = iconTint,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item.nombre,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "$ ${item.cantidadRequerida.formatMonto()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$ ${item.cantidadAcumulada.formatMonto()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Text(
                    text = "${"%.1f".format(progreso * 100)} %",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progreso },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(50)),
                color = iconTint,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.falta, restante.formatMonto()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

@Composable
fun EstadoVacio(
    mensaje: String,
    onCrearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.15f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.presiona_para_crear),
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF2196F3),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onCrearClick() }
        )
    }
}

@Composable
fun ResumenCards(
    totalLeft: Double,
    completados: Int,
    total: Int,
    labelCompletados: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.total_restante),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "-$ ${totalLeft.formatMonto()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLow,
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = labelCompletados,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$completados/$total",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

fun Double.formatMonto(): String {
    return if (this % 1.0 == 0.0) {
        "%,.0f".format(this)
    } else {
        "%,.2f".format(this)
    }
}



