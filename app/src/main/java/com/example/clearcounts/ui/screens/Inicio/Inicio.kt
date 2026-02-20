package com.example.clearcounts.ui.screens.Inicio

import android.R
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.TextStyle
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
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.ui.piechart.models.PieChartConfig
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

    var ingresoSeleccionado by remember { mutableStateOf<IncomeEntity?>(null) }
    var gastoSeleccionado by remember { mutableStateOf<ExpenseEntity?>(null) }

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

            item {
                GraficoLineChart()
            }

            item {
                Transacciones(
                    onVerTodoChange = {
                        //Todo funcion para implementar sobre el texto de ver todos en el inicio
                    }
                )
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
                    is IncomeEntity -> ItemIngreso(
                        ingreso = movimiento,
                        onPopUpIngreso = {
                            ingresoSeleccionado = movimiento
                        }
                    )
                    is ExpenseEntity -> ItemGasto(
                        gasto = movimiento,
                        onPopUpGasto = {
                            gastoSeleccionado = movimiento
                        }
                    )
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
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        },
        title = { Text(text = "Detalle de Ingreso", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Categoría: ${ingreso.categoria}")
                Text("Monto: ${ingreso.cantidad}")
                Text("Hora: ${ingreso.hora}")
                Text("Fecha: ${ingreso.fecha}")

                if (!ingreso.nota.isNullOrEmpty()) {
                    Text("Nota: ${ingreso.nota}")
                }

                Button(
                    onClick = {
                        onDelete()
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
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
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        },
        title = { Text(text = "Detalle de Gasto", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Categoría: ${gasto.categoria}")
                Text("Monto: ${gasto.cantidad}")
                Text("Hora: ${gasto.hora}")
                Text("Fecha: ${gasto.fecha}")

                if (!gasto.nota.isNullOrEmpty()) {
                    Text("Nota: ${gasto.nota}")
                }

                Button(
                    onClick = {
                        onDelete()
                        onDismiss()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
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
    ingreso: IncomeEntity,
    onPopUpIngreso:() -> Unit
){

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable{
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
    gasto: ExpenseEntity,
    onPopUpGasto: () -> Unit
){

    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable{
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
                    modifier = Modifier.size(24.dp).rotate(45f),
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
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Transacciones",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            modifier = Modifier.clickable{
                onVerTodoChange()
            },
            text = "Ver todas",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun GraficoLineChart(){

    val steps = 5

    val pointsData: List<Point> =
        listOf(Point(0f, 40f), Point(1f, 90f), Point(2f, 0f), Point(3f, 60f), Point(4f, 10f)
        )

    val xAxisData = AxisData.Builder()
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .axisStepSize(100.dp)
        .backgroundColor(color = MaterialTheme.colorScheme.background)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .steps(steps)
        .backgroundColor(color = MaterialTheme.colorScheme.background)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = 100 / steps
            (i * yScale).formatToSinglePrecision()
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(color = MaterialTheme.colorScheme.primaryContainer),
                    IntersectionPoint(color = MaterialTheme.colorScheme.primaryContainer),
                    SelectionHighlightPoint(color = MaterialTheme.colorScheme.onBackground),
                    ShadowUnderLine(color = MaterialTheme.colorScheme.primaryContainer, alpha = 0.5f),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = null,
        backgroundColor = MaterialTheme.colorScheme.background
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(2.dp),
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
