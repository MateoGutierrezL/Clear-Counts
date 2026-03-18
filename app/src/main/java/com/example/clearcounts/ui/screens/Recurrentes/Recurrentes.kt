package com.example.clearcounts.ui.screens.Recurrentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.clearcounts.ui.screens.Metas.formatMonto
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.utils.PaymentMethodTranslator
import com.example.clearcounts.data.local.database.entities.RecurringEntity
@Composable
fun ItemRecurrente(
    item: RecurringEntity,
    onToggle: (RecurringEntity) -> Unit
) {
    val esIngreso = item.tipo == "ingreso"
    val iconColor = if (esIngreso) Color(0xFF2ECC71) else Color(0xFFE74C3C)
    val containerColor = iconColor.copy(alpha = 0.15f)
    val icon = if (esIngreso) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icono estilo inicio
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(45f),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                // Nombre más grande
                Text(
                    text = item.nombre,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium  // <- más grande
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Categoría • Fecha
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.categoria,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    Text(
                        text = " • ",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )

                    Text(
                        text = stringResource(R.string.cada_dia, item.diaDelMes),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                // Método de pago
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = with(PaymentMethodTranslator) { context.traducirMetodoPago(item.metodoPago) },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "${if (esIngreso) "+" else "-"}$${item.cantidad.formatMonto()}",
                fontWeight = FontWeight.Bold,
                color = iconColor,
                style = MaterialTheme.typography.bodyLarge  // <- más grande
            )
            Spacer(modifier = Modifier.height(4.dp))
            Switch(
                checked = item.activo,
                onCheckedChange = { onToggle(item) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer  // <- primaryContainer
                )
            )
        }
    }
}

@Composable
fun Recurrentes(
    onBotonCrear: () -> Unit,
    viewModel: RecurringViewModel = hiltViewModel()
) {
    val ingresos by viewModel.ingresos.collectAsState()
    val gastos by viewModel.gastos.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onBotonCrear,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
                contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Ingresos recurrentes
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .background(Color(0xFF2ECC71), RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.ingresos_recurrentes),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            if (ingresos.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_hay_ingresos_recurrentes),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(ingresos) { item ->
                    ItemRecurrente(
                        item = item,
                        onToggle = { viewModel.toggleActivo(it) }
                    )
                }
            }

            // Gastos recurrentes
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(20.dp)
                            .background(Color(0xFFE74C3C), RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.gastos_recurrentes),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            if (gastos.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_hay_gastos_recurrentes),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(gastos) { item ->
                    ItemRecurrente(
                        item = item,
                        onToggle = { viewModel.toggleActivo(it) }
                    )
                }
            }
        }
    }
}