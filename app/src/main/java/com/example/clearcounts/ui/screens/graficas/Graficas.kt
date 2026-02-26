package com.example.clearcounts.ui.screens.graficas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.axis.AxisData
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
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Encabezado(
                titulo = "Gráficas",
                subtitulo = "Análisis de tus finanzas",
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
        modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ItemPromedio(
            titulo = "Promedio Diario",
            monto = ingresos,
            descripcion = "Ingresos",
            colorFondo = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )

        ItemPromedio(
            titulo = "Promedio Diario",
            monto = gastos,
            descripcion = "Gastos",
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
            PieChartData.Slice("Gastos", if (hayDatos) totalGastos.toFloat() else 0f, color = MaterialTheme.colorScheme.onError),
            PieChartData.Slice("Ingresos", if (hayDatos) totalIngresos.toFloat() else 0f, color = MaterialTheme.colorScheme.primaryContainer)
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
                text = "Ingresos vs Gastos",
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
                        text = "Sin datos aún",
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
                        text = "Ingresos"
                    )

                    Spacer(Modifier.width(16.dp))

                    LegendItem(
                        color = MaterialTheme.colorScheme.onError,
                        text = "Gastos"
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

    val etiquetasDias = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

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
                text = "Tendencia Semanal",
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
                LegendItem(color = colorIngresos, text = "Ingresos")
                Spacer(Modifier.width(16.dp))
                LegendItem(color = colorGastos, text = "Gastos")
            }
        }
    }
}


@Composable
fun GastosPorCategoria(){

    val pieChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("SciFi", 65f, Color(0xFF333333)),
            PieChartData.Slice("Comedy", 35f, Color(0xFF666a86)),
            PieChartData.Slice("Drama", 10f, Color(0xFF95B8D1)),
            PieChartData.Slice("Romance", 40f, Color(0xFFF53844))
        ),
        plotType = PlotType.Pie
    )

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
                text = "Gastos por categoría",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            PieChart(
                modifier = Modifier
                    .width(400.dp)
                    .height(280.dp),
                pieChartData,
                pieChartConfig
            )
        }
    }
}
