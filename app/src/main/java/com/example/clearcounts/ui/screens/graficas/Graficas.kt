package com.example.clearcounts.ui.screens.graficas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.common.utils.DataUtils.getColorPaletteList
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.GroupSeparatorConfig
import co.yml.charts.ui.piechart.charts.DonutPieChart
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
            val ingresoAvg = formatter.format(stats.dailyAverageIncome)
            val gastoAvg = formatter.format(stats.dailyAverageExpense)

            promediosDiarios(
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
fun promediosDiarios(
    ingresos: String,
    gastos: String
){
    Row(
        modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        itemPromedio(
            titulo = "Promedio Diario",
            monto = ingresos,
            descripcion = "Ingresos",
            colorFondo = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.weight(1f)
        )

        itemPromedio(
            titulo = "Promedio Diario",
            monto = gastos,
            descripcion = "Gastos",
            colorFondo = MaterialTheme.colorScheme.onError,
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
fun itemPromedio(
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
fun IngresosVsGastos(){
    val donutChartData = PieChartData(
        slices = listOf(
            PieChartData.Slice("Gastos", 110f, color = MaterialTheme.colorScheme.onError),
            PieChartData.Slice("Ingresos", 250f, color = MaterialTheme.colorScheme.primaryContainer)
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

            DonutPieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                donutChartData,
                donutChartConfig
            )
        }
    }
}

@Composable
fun TendenciaSemanal() {
    val maxRange = 100f
    val yStepSize = 5
    val semanas = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")

    // Colores para las barras (puedes usar los de tu Theme)
    val colorPrivado = MaterialTheme.colorScheme.primaryContainer
    val colorSecundario = MaterialTheme.colorScheme.onError

    // 1. Generar los datos de los grupos
    val groupBarList = semanas.mapIndexed { index, dia ->
        GroupBar(
            label = dia,
            barList = listOf(
                // El punto X debe coincidir con el índice del grupo
                BarData(
                    point = Point(index.toFloat(), (30..90).random().toFloat()),
                    color = colorPrivado
                ),
                BarData(
                    point = Point(index.toFloat(), (20..70).random().toFloat()),
                    color = colorSecundario
                )
            )
        )
    }

    val barStyle = BarStyle(
        paddingBetweenBars = 14.dp, // Espacio entre las 2 barras de un mismo día
        barWidth = 25.dp,          // Ancho de cada barra
        selectionHighlightData = null // Evita que se dibujen líneas al tocar
    )

    // 2. Configurar el PlotData (la lógica de dibujo de las barras)
    val groupBarPlotData = BarPlotData(
        groupBarList = groupBarList,
        barColorPaletteList = listOf(colorPrivado, colorSecundario),
        barStyle = barStyle

    )

    // 3. Configurar Eje X
    val xAxisData = AxisData.Builder()
        .axisLineColor(Color.Transparent)
        .backgroundColor(MaterialTheme.colorScheme.surfaceContainerLow)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .axisStepSize(60.dp) // Controla el ancho de cada grupo
        .steps(groupBarList.size - 1)
        .bottomPadding(12.dp)
        .labelData { index -> groupBarList[index].label }
        .build()

    // 4. Configurar Eje Y
    val yAxisData = AxisData.Builder()
        .axisLineColor(Color.Transparent)
        .backgroundColor(MaterialTheme.colorScheme.surfaceContainerLow)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground)
        .steps(yStepSize)
        .labelAndAxisLinePadding(15.dp)
        .labelData { index ->
            val labelValue = index * (maxRange / yStepSize)
            labelValue.toInt().toString()
        }
        .build()

    // 5. Unir todo en el Data Class final
    val groupBarChartData = GroupBarChartData(
        barPlotData = groupBarPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.surfaceContainerLow,
        groupSeparatorConfig = GroupSeparatorConfig(
            separatorColor = Color.Transparent  // ← Elimina las líneas grises
        ),
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
                text = "Tendencia semanal",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // El componente oficial de YCharts
            GroupBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(290.dp),
                groupBarChartData = groupBarChartData
            )
        }
    }
}
