package com.example.clearcounts.ui.screens.graficas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.HorizontalBarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.GroupSeparatorConfig
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.clearcounts.R
import java.text.NumberFormat
import java.util.Locale

@Preview
@Composable
fun GraficasPreview() {
    Graficas()
}


@Composable
fun Graficas(
    viewModel: GraficasViewModel = hiltViewModel()
){

    val formatter = NumberFormat.getInstance(Locale.forLanguageTag("es-ES")).apply {
        maximumFractionDigits = 0
    }

    val stats by viewModel.statsUiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Encabezado(
                titulo = stringResource(R.string.graficas),
                subtitulo = stringResource(R.string.analisis_de_tus_finanzas),
                 modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp)
            )
        }

        item {
            IngresosVsGastos()
        }

        item {
            TendenciaSemanal()
        }

        item {
            GastosPorCategoria()
        }

        item {
            ComparacionMensual()
        }

        item {
            val ingresoAvg = formatter.format(stats.dailyAverageIncome)
            val gastoAvg = formatter.format(stats.dailyAverageExpense)

            PromediosDiarios(
                ingresos = ingresoAvg,
                gastos = gastoAvg
            )
        }
    }
}

@Composable
fun Encabezado(
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier
){
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold,
        modifier = modifier
    )

    Text(
        text = subtitulo,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
fun PromediosDiarios(
    ingresos: String,
    gastos: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ItemPromedio(
            titulo = stringResource(R.string.promedio_diario),
            monto = ingresos,
            descripcion = stringResource(R.string.ingresos),
            colorFondo = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )

        ItemPromedio(
            titulo = stringResource(R.string.promedio_diario),
            monto = gastos,
            descripcion = stringResource(R.string.gastos),
            colorFondo = MaterialTheme.colorScheme.onError,
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
fun ItemPromedio(
    titulo: String,
    monto: String,
    descripcion: String,
    colorFondo: Color,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(colorFondo)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ){
        Text(
            text = titulo,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = monto,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun LegendItem(color: Color, text: String, textColor: Color = Color.Unspecified) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(width = 24.dp, height = 16.dp)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = textColor)
    }
}

@Composable
fun IngresosVsGastos(
    viewModel: GraficasViewModel = hiltViewModel()
){

    val totalIngresos by viewModel.totalIngresos.collectAsState()
    val totalGastos by viewModel.totalGastos.collectAsState()

    val hayDatos = totalIngresos > 0.0 || totalGastos > 0.0

    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice(stringResource(R.string.gastos), if (hayDatos) totalGastos.toFloat() else 0f, color = MaterialTheme.colorScheme.onError),
            PieChartData.Slice(stringResource(R.string.ingresos), if (hayDatos) totalIngresos.toFloat() else 0f, color = MaterialTheme.colorScheme.primaryContainer)
        ),
        plotType = PlotType.Donut
    )
    val donutChartConfig = PieChartConfig(
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
        strokeWidth = 100f,
        activeSliceAlpha = .9f,
        isAnimationEnable = true,
        labelVisible = true,
        chartPadding = 30
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start // Alinea el título a la izquierda
        ) {
            Text(
                text = stringResource(R.string.ingresos_vs_gastos),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (!hayDatos){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sin_datos_aun),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }else{
                DonutPieChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    donutChartData,
                    donutChartConfig
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        text = stringResource(R.string.ingresos)
                    )

                    Spacer(Modifier.width(16.dp))

                    LegendItem(
                        color = MaterialTheme.colorScheme.onError,
                        text = stringResource(R.string.gastos)
                    )
                }
            }
        }
    }
}

@Composable
fun TendenciaSemanal(
    viewModel: GraficasViewModel = hiltViewModel()
) {
    val ingresosPorDia by viewModel.ingresosPorDia.collectAsState()
    val gastosPorDia by viewModel.gastosPorDia.collectAsState()

    val colorIngresos = MaterialTheme.colorScheme.primaryContainer
    val colorGastos = MaterialTheme.colorScheme.onError

    val etiquetasDias = listOf(stringResource(R.string.lun),
        stringResource(R.string.mar), stringResource(R.string.mi),
        stringResource(R.string.jue), stringResource(R.string.vie),
        stringResource(R.string.sab), stringResource(R.string.dom)
    )

    // Calcula el máximo real para escalar el eje Y dinámicamente
    val todasLasCantidades = viewModel.diasSemana.flatMap { dia ->
        listOf(
            ingresosPorDia[dia] ?: 0.0,
            gastosPorDia[dia] ?: 0.0
        )
    }
    val maxValor = (todasLasCantidades.maxOrNull() ?: 100.0)
        .coerceAtLeast(100.0) // mínimo 100 para que no quede raro vacío
        .toFloat()

    val yStepSize = 5

    val groupBarList = viewModel.diasSemana.mapIndexed { index, dia ->
        val ingreso = (ingresosPorDia[dia] ?: 0.0).toFloat()
        val gasto = (gastosPorDia[dia] ?: 0.0).toFloat()

        GroupBar(
            label = etiquetasDias[index],
            barList = listOf(
                BarData(
                    point = Point(index.toFloat(), ingreso),
                    color = colorIngresos,
                    label = if (ingreso > 0) "${"%.0f".format(ingreso)}€" else ""
                ),
                BarData(
                    point = Point(index.toFloat(), gasto),
                    color = colorGastos,
                    label = if (gasto > 0) "${"%.0f".format(gasto)}€" else ""
                )
            )
        )
    }

    val barStyle = BarStyle(
        paddingBetweenBars = 12.dp,
        barWidth = 24.dp,
        selectionHighlightData = null
    )

    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarList,
        barColorPaletteList = listOf(colorIngresos, colorGastos),
        barStyle = barStyle
    )

    val xAxisData = AxisData.Builder()
        .axisLineColor(Color.Transparent)
        .backgroundColor(MaterialTheme.colorScheme.surfaceContainerLow)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .axisStepSize(70.dp)
        .steps(groupBarList.size - 1)
        .bottomPadding(12.dp)
        .labelData { index -> etiquetasDias[index] }
        .build()

    val yAxisData = AxisData.Builder()
        .axisLineColor(Color.Transparent)
        .backgroundColor(MaterialTheme.colorScheme.surfaceContainerLow)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .steps(yStepSize)
        .labelAndAxisLinePadding(15.dp)
        .labelData { index ->
            // Escala dinámica según el valor máximo real
            val step = maxValor / yStepSize
            (index * step).toInt().toString()
        }
        .build()

    val groupBarChartData = GroupBarChartData(
        barPlotData = groupBarPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
        groupSeparatorConfig = GroupSeparatorConfig(separatorColor = Color.Transparent),
        paddingEnd = 0.dp
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.tendencia_semanal),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            GroupBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp),
                groupBarChartData = groupBarChartData
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = colorIngresos, text = stringResource(R.string.ingresos))
                Spacer(Modifier.width(16.dp))
                LegendItem(color = colorGastos, text = stringResource(R.string.gastos))
            }
        }
    }
}

// Paleta de colores para asignar a cada slice dinámicamente
private val sliceColors = listOf(
    Color(0xFF006494),
    Color(0xFF007A73),
    Color(0xFF5E3585),
    Color(0xFF00695C),
    Color(0xFF1B5E20),
    Color(0xFF7A1E6D),
    Color(0xFF4A235A),
    Color(0xFF1A5276),
    Color(0xFF0E6655),
    Color(0xFF6C3483),
    Color(0xFF145A32),
    Color(0xFF784212),
    Color(0xFF1F618D),
    Color(0xFF5B2C6F),
    Color(0xFF0B5345),
    Color(0xFF922B21),
    Color(0xFF1A3A5C),
    Color(0xFF4A4A8A),
    Color(0xFF2E6B4F),
    Color(0xFF6B2D5E)
    )

@Composable
fun GastosPorCategoria(
    viewModel: GraficasViewModel = hiltViewModel()
) {
    val expensesByCategory by viewModel.expensesByCategory.collectAsStateWithLifecycle()

    val slices = remember(expensesByCategory) {
        expensesByCategory.mapIndexed { index, category ->
            PieChartData.Slice(
                label = category.categoria,
                value = category.total.toFloat(),
                color = sliceColors[index % sliceColors.size]
            )
        }
    }

    val pieChartConfig = PieChartConfig(
        isAnimationEnable = true,
        showSliceLabels = false,
        animationDuration = 1300,
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
        chartPadding = 10
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.gastos_por_categoria),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            when {
                // Estado vacío: no hay gastos registrados aún
                slices.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.sin_gastos_registrados),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    PieChart(
                        modifier = Modifier
                            .width(400.dp)
                            .height(280.dp),
                        pieChartData = PieChartData(
                            slices = slices,
                            plotType = PlotType.Pie
                        ),
                        pieChartConfig = pieChartConfig
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Leyenda dinámica
                    slices.forEachIndexed { index, slice ->
                        val categoria = expensesByCategory[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(slice.color, CircleShape)
                                )
                                Text(
                                    text = categoria.categoria,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Text(
                                text = "${categoria.total.toInt()}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ComparacionMensual(
    viewModel: GraficasViewModel = hiltViewModel()
) {
    val monthlyData by viewModel.monthlyComparison.collectAsStateWithLifecycle()

    val colorIngresos = MaterialTheme.colorScheme.primaryContainer
    val colorGastos = MaterialTheme.colorScheme.onError

    // Mapeo de MM/yyyy → nombre de mes recortado
    val nombreMes = mapOf(
        "01" to stringResource(R.string.ene), "02" to stringResource(R.string.feb), "03" to stringResource(
            R.string.marzo
        ),
        "04" to stringResource(R.string.abr), "05" to stringResource(R.string.may), "06" to stringResource(
            R.string.jun
        ),
        "07" to stringResource(R.string.jul), "08" to stringResource(R.string.ago), "09" to stringResource(
            R.string.sep
        ),
        "10" to stringResource(R.string.oct), "11" to stringResource(R.string.nov), "12" to stringResource(
            R.string.dic
        )
    )

    val todasLasCantidades = monthlyData.flatMap {
        listOf(it.totalIngreso, it.totalGasto)
    }
    val maxValor = (todasLasCantidades.maxOrNull() ?: 100.0)
        .coerceAtLeast(100.0)
        .toFloat()

    val yStepSize = 5

    val groupBarList = monthlyData.mapIndexed { index, item ->
        val ingreso = item.totalIngreso.toFloat()
        val gasto = item.totalGasto.toFloat()
        val mesLabel = nombreMes[item.mes.substring(0, 2)] ?: item.mes

        GroupBar(
            label = mesLabel,
            barList = listOf(
                BarData(
                    point = Point(index.toFloat(), ingreso),
                    color = colorIngresos,
                    label = if (ingreso > 0) "${"%.0f".format(ingreso)}€" else ""
                ),
                BarData(
                    point = Point(index.toFloat(), gasto),
                    color = colorGastos,
                    label = if (gasto > 0) "${"%.0f".format(gasto)}€" else ""
                )
            )
        )
    }

    val barStyle = BarStyle(
        paddingBetweenBars = 12.dp,
        barWidth = 24.dp,
        selectionHighlightData = null
    )

    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarList,
        barColorPaletteList = listOf(colorIngresos, colorGastos),
        barStyle = barStyle
    )

    val xAxisData = AxisData.Builder()
        .axisLineColor(Color.Transparent)
        .backgroundColor(MaterialTheme.colorScheme.surfaceContainerLow)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .axisStepSize(70.dp)
        .steps(groupBarList.size - 1)
        .bottomPadding(12.dp)
        .labelData { index -> groupBarList[index].label }
        .build()

    val yAxisData = AxisData.Builder()
        .axisLineColor(Color.Transparent)
        .backgroundColor(MaterialTheme.colorScheme.surfaceContainerLow)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .steps(yStepSize)
        .labelAndAxisLinePadding(15.dp)
        .labelData { index ->
            val step = maxValor / yStepSize
            (index * step).toInt().toString()
        }
        .build()

    val groupBarChartData = GroupBarChartData(
        barPlotData = groupBarPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
        groupSeparatorConfig = GroupSeparatorConfig(separatorColor = Color.Transparent),
        paddingEnd = 0.dp
    )

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.comparacion_mensual),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (monthlyData.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.sin_datos_registrados),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                GroupBarChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(290.dp),
                    groupBarChartData = groupBarChartData
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = colorIngresos, text = stringResource(R.string.ingresos))
                Spacer(Modifier.width(16.dp))
                LegendItem(color = colorGastos, text = stringResource(R.string.gastos))
            }
        }
    }
}
