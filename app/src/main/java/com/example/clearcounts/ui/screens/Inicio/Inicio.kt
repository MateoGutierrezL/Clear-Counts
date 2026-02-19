package com.example.clearcounts.ui.screens.Inicio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues, viewModel: InicioViewModel = hiltViewModel()
) {

    val listaMovimientos by viewModel.movimientosState.collectAsState()

    val formatoFecha: DateTimeFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale.forLanguageTag("es-ES"))

    var fechaActual by remember { mutableStateOf(LocalDate.now().format(formatoFecha)) }

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item {
                Balance(
                    fechaActual = fechaActual
                )
            }

            item {
                IngresosGastos()
            }
            items(
                items = listaMovimientos,
                // Clave única para que Compose no se confunda con IDs repetidos entre tablas
                key = { item ->
                    when (item) {
                        is IncomeEntity -> "inc_${item.id}"
                        is ExpenseEntity -> "exp_${item.id}"
                        else -> item.hashCode()
                    }
                }
            ) { movimiento ->
                when (movimiento) {
                    is IncomeEntity -> ItemIngreso(ingreso = movimiento)
                    is ExpenseEntity -> ItemGasto(gasto = movimiento)
                }
            }
            item {
                contenidoAbajo() // Este contiene la parte de metas
            }
        }
}

@Composable
fun Balance(
    fechaActual: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(text = fechaActual, style = MaterialTheme.typography.bodyMedium)
        }

        Text(text = "Balance Total", style = MaterialTheme.typography.bodyLarge)

        Text(
            text = "2555,00 €",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun IngresosGastos(){

    Row(modifier =
        Modifier.fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ItemFinanciero(
            titulo = "Ingresos",
            monto = "3200,00 €",
            icono = Icons.AutoMirrored.Default.TrendingUp,
            modifier = Modifier.weight(1f),
            tint = Color(0xFF2ECC71)
        )

        ItemFinanciero(
            titulo = "Gastos",
            monto = "645,00 €",
            icono = Icons.AutoMirrored.Default.TrendingDown,
            modifier = Modifier.weight(1f),
            tint = Color(0xFFE74C3C)
        )
    }
}

@Composable
fun ItemFinanciero(
    titulo: String,
    monto: String,
    icono: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

             Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icono, contentDescription = null, modifier = Modifier.size(20.dp), tint = tint)
            }
            Text(text = titulo, style = MaterialTheme.typography.bodyMedium)
        }

        Text(
            text = monto,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ItemIngreso(
    ingreso: IncomeEntity
){

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val containerColor = Color.Black.copy(alpha = 0.1f)
            val iconColor =  Color(0xFF2ECC71)
            val icon = Icons.Default.ArrowUpward

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).rotate(45f),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = ingreso.categoria,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!ingreso.nota.isNullOrBlank()){
                    Text(
                        text = ingreso.nota,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "+${ingreso.cantidad.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2ECC71),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun ItemGasto(
    gasto: ExpenseEntity
){

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val containerColor = Color.Black.copy(alpha = 0.1f)
            val iconColor =  Color(0xFFE74C3C)
            val icon = Icons.Default.ArrowDownward

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(containerColor),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp).rotate(225f),
                    tint = iconColor
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = gasto.categoria,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!gasto.nota.isNullOrBlank()){
                    Text(
                        text = gasto.nota,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "-${gasto.cantidad.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFE74C3C),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun GraficoBarras(
    datos: List<Float>
){
    val maxValor = datos.maxOrNull() ?: 1f // Para normalizar la altura

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        val anchoLienzo = size.width
        val altoLienzo = size.height
        val espacioEntreBarras = 20f
        val anchoBarra = (anchoLienzo - (espacioEntreBarras * (datos.size - 1))) / datos.size

        datos.forEachIndexed { index, valor ->
            // 1. Calcular la altura proporcional
            val alturaBarra = (valor / maxValor) * altoLienzo

            // 2. Dibujar el rectángulo
            drawRect(
                color = Color(0xFF3498DB),
                topLeft = Offset(
                    x = index * (anchoBarra + espacioEntreBarras),
                    y = altoLienzo - alturaBarra // Invertir el eje Y
                ),
                size = Size(anchoBarra, alturaBarra)
            )
        }
    }
}

@Composable
fun LineChart(data: List<Float>) {
    Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
        val path = Path()
        val width = size.width
        val height = size.height
        val maxData = data.maxOrNull() ?: 1f
        val minData = data.minOrNull() ?: 0f
        val range = maxData - minData

        // Calcular puntos y dibujar línea
        data.forEachIndexed { index, value ->
            val x = index * (width / (data.size - 1))
            val y = height - ((value - minData) / range * height)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = Color.Blue,
            style = Stroke(width = 5f)
        )
    }
}
@Composable
fun contenidoAbajo(){

}