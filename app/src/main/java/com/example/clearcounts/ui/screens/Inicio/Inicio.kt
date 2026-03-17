package com.example.clearcounts.ui.screens.Inicio

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.ConfigurationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
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
import com.example.clearcounts.data.database.entities.PaymentMethodEntity
import com.example.clearcounts.utils.CategoryTranslator.traducirCategoria
import com.example.clearcounts.utils.PaymentMethodTranslator.traducirMetodoPago
import com.facebook.internal.Utility.locale
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@SuppressLint("LocalContextConfigurationRead")
@Composable
fun HomeScreen(
    viewModel: InicioViewModel = hiltViewModel(),
    navegarTodasTransacciones: () -> Unit,
    navegarTransaccionesPorMetodo: (String) -> Unit
) {

    val formatter = NumberFormat.getInstance(Locale.forLanguageTag("es-ES")).apply {
        maximumFractionDigits = 0
    }

    val context = LocalContext.current

    val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        ?: Locale.getDefault()
    val isSpanish = locale.language == "es"
    val pattern = if (isSpanish) "dd 'de' MMMM" else "MMMM dd"
    val formatoFecha = DateTimeFormatter.ofPattern(pattern, locale)

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
    val balancePorMetodo by viewModel.balancePorMetodoPago.collectAsState()
    val metodosPago by viewModel.metodosPago.collectAsState()


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

            item{
                if (balancePorMetodo.isNotEmpty()) {
                    Text(
                        text = "Métodos de pago",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(balancePorMetodo.entries.toList()) { (metodo, iconoYBalance) ->
                            val (icono, balance) = iconoYBalance
                            TarjetaMetodoPago(
                                nombre = metodo,
                                balance = balance,
                                icono = icono,
                                formatter = formatter,
                                onClick = {
                                    navegarTransaccionesPorMetodo(metodo)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
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
                        text = formatearFechaHeader(item.fecha, context),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )
                }
                is InicioViewModel.MovimientoItem.Transaccion -> {
                    when (val mov = item.movimiento) {
                        is IncomeEntity -> ItemIngreso(
                            ingreso = mov,
                            metodosPago = metodosPago,
                            onPopUpIngreso = { ingresoSeleccionado = mov }
                        )
                        is ExpenseEntity -> ItemGasto(
                            gasto = mov,
                            metodosPago = metodosPago,
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

    val context = LocalContext.current

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
                Text(stringResource(R.string.categoria, context.traducirCategoria(ingreso.categoria)))
                Text(stringResource(R.string.monto, ingreso.cantidad.toInt()))
                Text(stringResource(R.string.metodo_pago, context.traducirMetodoPago(ingreso.metodoPago)))
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

    val context = LocalContext.current

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
            Text(stringResource(R.string.categoria, context.traducirCategoria(gasto.categoria)))
            Text(stringResource(R.string.monto, gasto.cantidad.toInt()))
            Text(stringResource(R.string.metodo_pago, context.traducirMetodoPago(gasto.metodoPago)))
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
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
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
    metodosPago: List<PaymentMethodEntity>,
    onPopUpIngreso:() -> Unit
){

    val context = LocalContext.current

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
                    text = context.traducirCategoria(ingreso.categoria),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                MetodoPagoChip(
                    metodoPago = ingreso.metodoPago,
                    icono = metodosPago.find { it.nombre == ingreso.metodoPago }?.icono ?: "efectivo" // 👈
                )
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
    metodosPago: List<PaymentMethodEntity>,
    onPopUpGasto: () -> Unit
){

    val context = LocalContext.current

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
                    text = context.traducirCategoria(gasto.categoria),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                MetodoPagoChip(
                    metodoPago = gasto.metodoPago,
                    icono = metodosPago.find { it.nombre == gasto.metodoPago }?.icono ?: "efectivo"
                )
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

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun GraficoLineChart(
    data: Triple<List<Point>, List<String>, List<Float>>,
    context: Context = LocalContext.current
) {
    val pointsData = data.first
    val labelsX = data.second
    val yValues = data.third

    if (pointsData.isEmpty()) return

    val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        ?: Locale.getDefault()
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

fun formatearFechaHeader(fecha: String, context: Context): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = LocalDate.parse(fecha, formatter)
        val hoy = LocalDate.now()
        val ayer = hoy.minusDays(1)
        when (date) {
            hoy  -> context.getString(R.string.hoy)
            ayer -> context.getString(R.string.ayer)
            else -> {
                val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
                    ?: Locale.getDefault()
                val isSpanish = locale.language == "es"
                val pattern = when {
                    isSpanish && date.year == hoy.year  -> "dd 'de' MMMM"
                    isSpanish && date.year != hoy.year  -> "dd 'de' MMMM 'de' yyyy"
                    !isSpanish && date.year == hoy.year -> "MMMM dd"
                    else                                -> "MMMM dd, yyyy"
                }
                date.format(DateTimeFormatter.ofPattern(pattern, locale))
            }
        }
    } catch (e: Exception) {
        fecha
    }

}


@Composable
fun TarjetaMetodoPago(
    nombre: String,
    balance: Double,
    icono: String,
    formatter: NumberFormat,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val iconoId = remember(icono) {
        context.resources.getIdentifier(icono, "drawable", context.packageName)
    }

    val gradientColors = remember(nombre) {
        val gradientes = listOf(
            listOf(Color(0xFF1A237E), Color(0xFF283593)), // azul oscuro
            listOf(Color(0xFF1B5E20), Color(0xFF2E7D32)), // verde oscuro
            listOf(Color(0xFF4A148C), Color(0xFF6A1B9A)), // púrpura
            listOf(Color(0xFF880E4F), Color(0xFFAD1457)), // rosa oscuro
            listOf(Color(0xFF004D40), Color(0xFF00695C)), // teal oscuro
            listOf(Color(0xFF33691E), Color(0xFF558B2F)), // verde oliva
            listOf(Color(0xFFBF360C), Color(0xFFD84315)), // naranja oscuro
            listOf(Color(0xFF1565C0), Color(0xFF1976D2)), // azul medio
        )
        gradientes[Math.abs(nombre.hashCode()) % gradientes.size]
    }

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(colors = gradientColors)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header con icono y nombre
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (iconoId != 0) {
                    Image(
                        painter = painterResource(id = iconoId),
                        contentDescription = nombre,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = context.traducirMetodoPago(nombre),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Balance
            Column {
                Text(
                    text = "Balance",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
                Text(
                    text = formatter.format(balance.toInt()),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Círculos decorativos estilo tarjeta
        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = 130.dp, y = (-20).dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(60.dp)
                .offset(x = 150.dp, y = 20.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.05f))
        )
    }
}

@Composable
fun MetodoPagoChip(metodoPago: String, icono: String) {
    val context = LocalContext.current
    val iconoId = remember(icono) {
        context.resources.getIdentifier(icono, "drawable", context.packageName)
    }

    Row(
        modifier = Modifier
            .padding(top = 4.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (iconoId != 0) {
            Image(
                painter = painterResource(id = iconoId),
                contentDescription = null,
                modifier = Modifier.size(12.dp)
            )
        }
        Text(
            text = context.traducirMetodoPago(metodoPago),
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun TransaccionesPorMetodo(
    metodoPago: String,
    botonVolver: () -> Unit,
    viewModel: InicioViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val movimientosFiltrados by viewModel.movimientosAgrupadosSinLimite.collectAsState()
    var ingresoSeleccionado by remember { mutableStateOf<IncomeEntity?>(null) }
    var gastoSeleccionado by remember { mutableStateOf<ExpenseEntity?>(null) }
    val metodosPago by viewModel.metodosPago.collectAsState()

    // Filtra solo los del método de pago seleccionado
    val movimientosPorMetodo = remember(movimientosFiltrados, metodoPago) {
        movimientosFiltrados.filter { item ->
            when (item) {
                is InicioViewModel.MovimientoItem.Header -> {
                    // Incluir header solo si tiene transacciones del método
                    true
                }
                is InicioViewModel.MovimientoItem.Transaccion -> {
                    when (val mov = item.movimiento) {
                        is IncomeEntity -> mov.metodoPago == metodoPago
                        is ExpenseEntity -> mov.metodoPago == metodoPago
                        else -> false
                    }
                }
            }
        }.filterIndexed { index, item ->
            // Eliminar headers que no tienen transacciones después
            if (item is InicioViewModel.MovimientoItem.Header) {
                val siguiente = movimientosFiltrados.getOrNull(index + 1)
                siguiente is InicioViewModel.MovimientoItem.Transaccion
            } else true
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = botonVolver) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = context.traducirMetodoPago(metodoPago),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider()

        if (movimientosPorMetodo.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Inbox,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No hay registros para ${context.traducirMetodoPago(metodoPago)}",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                }
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = movimientosPorMetodo,
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
                            Text(
                                text = formatearFechaHeader(item.fecha, context),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                            )
                        }
                        is InicioViewModel.MovimientoItem.Transaccion -> {
                            when (val mov = item.movimiento) {
                                is IncomeEntity -> ItemIngreso(
                                    ingreso = mov,
                                    metodosPago = metodosPago,
                                    onPopUpIngreso = { ingresoSeleccionado = mov }
                                )
                                is ExpenseEntity -> ItemGasto(
                                    gasto = mov,
                                    metodosPago = metodosPago,
                                    onPopUpGasto = { gastoSeleccionado = mov }
                                )
                            }
                        }
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
                ingresoSeleccionado = null
            }
        )
    }

    gastoSeleccionado?.let { gasto ->
        DetalleGastoPopup(
            gasto = gasto,
            onDismiss = { gastoSeleccionado = null },
            onDelete = {
                viewModel.deleteExpense(gasto)
                gastoSeleccionado = null
            }
        )
    }
}


fun obtenerIcono(metodoPago: String, metodos: List<PaymentMethodEntity>): String {
    return metodos.find { it.nombre == metodoPago }?.icono ?: "efectivo"
}