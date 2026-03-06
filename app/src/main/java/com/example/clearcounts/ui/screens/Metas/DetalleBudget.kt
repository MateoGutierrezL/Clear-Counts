package com.example.clearcounts.ui.screens.Metas

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.data.database.entities.BudgetEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetalleBudget(
    budget: BudgetEntity,
    onVolver: () -> Unit,
    onEditar: (BudgetEntity) -> Unit,
    viewModel: MetasViewModel = hiltViewModel()
) {
    val formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formatoMostrar = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es"))

    fun formatearFecha(fecha: String?): String? {
        if (fecha.isNullOrBlank()) return null
        return try {
            LocalDate.parse(fecha, formatoFecha).format(formatoMostrar)
        } catch (e: Exception) { null }
    }

    val fechaInicio = formatearFecha(budget.fechaInicio)
    val fechaLimite = formatearFecha(budget.fechaLimite)

    Scaffold(
        bottomBar = {
            Button(
                onClick = { onVolver() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text("OK", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {

            // Barra superior
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }

                TextButton(
                    onClick = { onEditar(budget) },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "Edit",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Título y nota
            Text(
                text = budget.nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (!budget.nota.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = budget.nota,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjetas de montos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cantidad acumulada
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Cantidad\nacumulada",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$ ${budget.cantidadAcumulada.formatMonto()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                // Total requerido
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
                        text = "Total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$ ${budget.cantidadRequerida.formatMonto()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Campos opcionales
            FilaDetalle(
                label = "Fecha",
                valor = fechaInicio
            )

            if (!budget.prestador.isNullOrBlank()) {
                FilaDetalle(
                    label = "Prestador",
                    valor = budget.prestador,
                    esTextoSimple = true
                )
            }

            FilaDetalle(
                label = "Fecha límite",
                valor = fechaLimite
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botones eliminar y marcar como hecho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.eliminarBudget(budget)
                        onVolver()
                    },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFE74C3C)
                    )
                }

                Button(
                    onClick = { viewModel.marcarComoHecho(budget) },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Text(
                        text = "Marcar como completado",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No hay transacciones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun FilaDetalle(
    label: String,
    valor: String?,
    esTextoSimple: Boolean = false
) {
    if (valor.isNullOrBlank()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        if (esTextoSimple) {
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        } else {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = valor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}