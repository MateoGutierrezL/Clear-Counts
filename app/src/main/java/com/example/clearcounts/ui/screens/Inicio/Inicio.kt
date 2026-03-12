package com.example.clearcounts.ui.screens.Inicio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.example.clearcounts.R
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.facebook.internal.Utility.locale
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun HomeScreen(
    paddingValues: PaddingValues, viewModel: InicioViewModel = hiltViewModel(), navegarTodasTransacciones: () -> Unit
) {

    val formatter = NumberFormat.getInstance(Locale.forLanguageTag("es-ES")).apply {
        maximumFractionDigits = 0
    }

    val formatoFecha: DateTimeFormatter = DateTimeFormatter.ofPattern("dd 'de' MMMM", Locale.forLanguageTag("es-ES"))

    var fechaActual by remember { mutableStateOf(LocalDate.now().format(formatoFecha)) }

    var ingresoSeleccionado by remember { mutableStateOf<IncomeEntity?>(null) }
    var gastoSeleccionado by remember { mutableStateOf<ExpenseEntity?>(null) }

    val totalIngreso by viewModel.totalIngresoSum.collectAsState()

    val totalGasto by viewModel.totalGastoSum.collectAsState()

    val chartData by viewModel.chartDataState.collectAsState()

    val movimientosAgrupados by viewModel.movimientosAgrupados.collectAsState()

    val totalIngresoMensual by viewModel.totalIngresoMensual.collectAsState()
    val totalGastoMensual by viewModel.totalGastoMensual.collectAsState()
    val balanceMensual by viewModel.balanceMensual.collectAsState()

    LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            item {
                val balance = (totalIngreso-totalGasto).toInt()

                Balance(
                    fechaActual = fechaActual,
                    balance = formatter.format(balanceMensual.toInt())
                )
            }

            item {
                IngresosGastos(
                    totalIngreso = formatter.format(totalIngresoMensual.toInt()),
                    totalGasto = formatter.format(totalGastoMensual.toInt())
                )
            }

            item {
                Text(
                    text = stringResource(R.string.ultimos_7_dias),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp)
                )
                GraficoLineChart(data = chartData)
            }

            item {
                Transacciones(
                    onVerTodoChange = {
                        navegarTodasTransacciones()
                    }
                )
            }
        items(
            items = movimientosAgrupados,
            key = { item ->
                when (item) {
                    is InicioViewModel.MovimientoItem.Header -> "header_${item.fecha}"
                    is InicioViewModel.MovimientoItem.Transaccion -> when (val mov = item.movimiento) {
                        is IncomeEntity -> "inc_${mov.id}"
                        is ExpenseEntity -> "exp_${mov.id}"
                        else -> item.hashCode()
                    }
                }
            }
        ) { item ->
            when (item) {
                is InicioViewModel.MovimientoItem.Header -> {
                    // 👇 Header de fecha
                    Text(
                        text = formatearFechaHeader(item.fecha),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
                is InicioViewModel.MovimientoItem.Transaccion -> {
                    when (val mov = item.movimiento) {
                        is IncomeEntity -> ItemIngreso(
                            ingreso = mov,
                            onPopUpIngreso = { ingresoSeleccionado = mov }
                        )
                        is ExpenseEntity -> ItemGasto(
                            gasto = mov,
                            onPopUpGasto = { gastoSeleccionado = mov }
                        )
                    }
                }
            }
        }
        }

    ingresoSeleccionado?.let { ingreso ->
        DetalleIngresoPopup(
            ingreso = ingreso,
            onDismiss = { ingresoSeleccionado = null },
            onDelete = {
                viewModel.deleteIncome(ingreso)
            }
        )
    }

    gastoSeleccionado?.let { gasto ->
        DetalleGastoPopup(
            gasto = gasto,
            onDismiss = { gastoSeleccionado = null },
            onDelete = {
                viewModel.deleteExpense(gasto)
            }
        )
    }
}

@Composable
fun DetalleIngresoPopup(
    ingreso: IncomeEntity,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cerrar)) }
        },
        dismissButton = {
            Box(
                modifier = Modifier.fillMaxWidth(0.7f)
            ){
                IconButton(
                    onClick = {
                        onDelete()
                        onDismiss()
                    }

                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.eliminar)
                    )
                }
            }
        },
        title = { Text(text = stringResource(R.string.detalle_de_ingreso), fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(stringResource(R.string.categoria, ingreso.categoria))
                Text(stringResource(R.string.monto, ingreso.cantidad.toInt()))
                Text(stringResource(R.string.hora, ingreso.hora))
                Text(stringResource(R.string.fecha, ingreso.fecha))

                if (!ingreso.nota.isNullOrEmpty()) {
                    Text(stringResource(R.string.nota, ingreso.nota))
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun DetalleGastoPopup(
    gasto: ExpenseEntity,
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
        TextButton(onClick = onDismiss) { Text(stringResource(R.string.cerrar)) }
    },
    dismissButton = {
        Box(
            modifier = Modifier.fillMaxWidth(0.7f)
        ){
            IconButton(
                onClick = {
                    onDelete()
                    onDismiss()
                }

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.eliminar)
                )
            }
        }
    },
    title = { Text(text = stringResource(R.string.detalle_de_gasto), fontWeight = FontWeight.Bold) },
    text = {
        Column {
            Text(stringResource(R.string.categoria, gasto.categoria))
            Text(stringResource(R.string.monto, gasto.cantidad.toInt()))
            Text(stringResource(R.string.hora, gasto.hora))
            Text(stringResource(R.string.fecha, gasto.fecha))

            if (!gasto.nota.isNullOrEmpty()) {
                Text(stringResource(R.string.nota, gasto.nota))
            }
        }
    },
    shape = RoundedCornerShape(16.dp)
)
}

@Composable
fun Balance(
    fechaActual: String,
    balance: String
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

        Text(text = stringResource(R.string.balance_del_mes), style = MaterialTheme.typography.bodyLarge)

        Text(
            text = balance,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun IngresosGastos(
    totalIngreso: String,
    totalGasto: String,
){

    Row(modifier =
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ItemFinanciero(
            titulo = stringResource(R.string.ingresos),
            monto = totalIngreso,
            icono = Icons.AutoMirrored.Default.TrendingUp,
            modifier = Modifier.weight(1f),
            tint = Color(0xFF2ECC71)
        )

        ItemFinanciero(
            titulo = stringResource(R.string.gastos),
            monto = totalGasto,
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
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                RoundedCornerShape(16.dp)
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
    ingreso: IncomeEntity,
    onPopUpIngreso:() -> Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onPopUpIngreso()
            },
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
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(45f),
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
    gasto: ExpenseEntity,
    onPopUpGasto: () -> Unit
){

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable {
                onPopUpGasto()
            },
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
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(45f),
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
fun Transacciones(
    onVerTodoChange:() -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.transacciones),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier.clickable{
                onVerTodoChange()
            },
            text = stringResource(R.string.ver_todas),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun GraficoLineChart(data: Triple<List<Point>, List<String>, List<Float>>) {
    val pointsData = data.first
    val labelsX = data.second
    val yValues = data.third

    if (pointsData.isEmpty()) return

    val locale = Locale.forLanguageTag("es-ES")
    val currencyFormat = NumberFormat.getInstance(locale).apply {
        maximumFractionDigits = 0
    }

    // CALCULAMOS EL RANGO REAL
    val minY = yValues.first()
    val maxY = yValues.last()

    // Usaremos 4 o 5 pasos fijos para que el eje Y sea limpio
    val steps = 4

    val yAxisData = AxisData.Builder()
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            // Calculamos etiquetas proporcionales para que solo haya un 0
            val range = maxY - minY
            val value = minY + (i * (range / steps))
            currencyFormat.format(value.toInt())
        }
        .build()

    val xAxisData = AxisData.Builder()
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .axisStepSize(85.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> labelsX.getOrElse(i) { "" } }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(color = MaterialTheme.colorScheme.primaryContainer),
                    intersectionPoint = IntersectionPoint(color = MaterialTheme.colorScheme.primaryContainer),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.onBackground),
                    ShadowUnderLine(color = MaterialTheme.colorScheme.primaryContainer, alpha = 0.5f),
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        popUpLabel = { _, y -> "${y.toInt()}" }
                    )
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surface
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}

fun Float.formatToSinglePrecision(): String {
    return String.format("%.1f", this)
}

// O si el resultado de tu operación es Int, úsala sobre Floats:
fun Number.formatToSinglePrecision(): String {
    return "%.1f".format(this.toDouble())
}

fun formatearFechaHeader(fecha: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(fecha, formatter)
        val hoy = LocalDate.now()
        val ayer = hoy.minusDays(1)
        when (date) {
            hoy -> "Hoy"
            ayer -> "Ayer"
            else -> {
                val pattern = if (date.year == hoy.year) {
                    "dd 'de' MMMM" // 👈 Sin año si es el mismo año
                } else {
                    "dd 'de' MMMM 'de' yyyy" // 👈 Con año si es diferente
                }
                date.format(DateTimeFormatter.ofPattern(pattern, Locale.forLanguageTag("es-ES")))
            }
        }
    } catch (e: Exception) {
        fecha
    }
}
