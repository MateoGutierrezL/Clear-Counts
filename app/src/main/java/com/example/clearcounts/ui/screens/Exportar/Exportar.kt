package com.example.clearcounts.ui.screens.Exportar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.ConfigurationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.clearcounts.R
import com.example.clearcounts.data.local.database.entities.ExpenseEntity
import com.example.clearcounts.data.local.database.entities.IncomeEntity
import com.example.clearcounts.ui.screens.Ajustes.ThemeViewModel
import com.example.clearcounts.utils.CategoryTranslator.traducirCategoria
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PantallaExportarGrafico(
    viewModel: ExportarGraficosViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel
){
    val ingresosTotales by viewModel.totalIngresosMensuales.collectAsState()
    val gastosTotales by viewModel.totalGastosMensuales.collectAsState()
    val mesFiltro by viewModel.mesSeleccionado.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
    val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle()
    var pdf by rememberSaveable {
        mutableStateOf(true)
    }
    var csv by rememberSaveable {
        mutableStateOf(false)
    }
    val colorBotonpdf = if (isSystemInDarkTheme()) {
        if (pdf) MaterialTheme.colorScheme.primary else Color.Black
    } else {
        if (pdf) Color.Black else Color.Black
    }
    val colorBotoncsv = if (isSystemInDarkTheme()) {
        if (csv) Color.Cyan else Color.Black
    } else {
        if (csv) Color.Black else Color.Black
    }
    val ingresosPorMes by viewModel.ingresosPorMes.collectAsState()
    val gastosPorCategoria by viewModel.gastosPorCategoria.collectAsState()
    val comparacionMensual by viewModel.comparacionMensual.collectAsState()
    val totalIngresoVsGasto by viewModel.totalIngresoVsGastoMes.collectAsState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let { seleccionadoUri ->
            coroutineScope.launch {
                try {
                    delay(150)
                    val hardwareBitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                    guardarPdfEnUri(context, hardwareBitmap, seleccionadoUri)
                    viewModel.notificarExportacion("PDF")
                } catch (e: Exception) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    // Variables del csv
    val movimientos by viewModel.movimientosFiltrados.collectAsState()

    // Estado para el CSV
    var csvContent by remember { mutableStateOf("") }

    // Lanzador para guardar el archivo
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { output ->
                output.write(csvContent.toByteArray())
            }
            viewModel.notificarExportacion("CSV")
            Toast.makeText(context,
                context.getString(R.string.guardado_con_exito), Toast.LENGTH_SHORT).show()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- ZONA DE CAPTURA ---
        Text(
            text = stringResource(R.string.vista_previa_del_documento),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (pdf) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isDarkMode) MaterialTheme.colorScheme.surfaceContainerLow
                        else Color.White
                    )
                    .padding(8.dp)
            ) {
                ContenidoPDF(
                    mesFiltro = mesFiltro,
                    ingresosTotales = ingresosTotales,
                    gastosTotales = gastosTotales,
                    ingresosPorMes = ingresosPorMes,
                    gastosPorCategoria = gastosPorCategoria,
                    comparacionMensual = comparacionMensual,
                    totalIngresoVsGasto = totalIngresoVsGasto,
                    isDarkTheme = isDarkMode
                )
            }

            //Caja invisible solo para captura — siempre blanca
            Box(
                modifier = Modifier
                    .layout { measurable, constraints ->
                        val placeable = measurable.measure(constraints.copy(minHeight = 0))
                        layout(0, 0) {
                            placeable.place(0, 0)
                        }
                    }
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                    }
            ) {
                Box(modifier = Modifier.background(Color.White)) {
                    ContenidoPDF(
                        mesFiltro = mesFiltro,
                        ingresosTotales = ingresosTotales,
                        gastosTotales = gastosTotales,
                        ingresosPorMes = ingresosPorMes,
                        gastosPorCategoria = gastosPorCategoria,
                        comparacionMensual = comparacionMensual,
                        totalIngresoVsGasto = totalIngresoVsGasto,
                        isDarkTheme = false
                    )
                }
            }
        }else{
            VistaPreviaCSV(movimientos)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.Start) {
            Button(
                onClick = {
                    pdf = true
                    csv = false
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (pdf) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = "PDF",
                    color = colorBotonpdf
                )
            }
            Button(
                onClick = {
                    pdf = false
                    csv = true
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (csv) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = "CSV",
                    color = colorBotoncsv
                )
            }
        }

        SelectorMesAnio(
            mesAnioActual = mesFiltro,
            onMesSeleccionado = { nuevoMes ->
                viewModel.setMesFiltro(nuevoMes)
            }
        )

        if (pdf){
            Button(
                onClick = { launcher.launch("Report_${System.currentTimeMillis()}.pdf") },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(stringResource(R.string.guardar_pdf), color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }else{
            Button(
                onClick = {
                    if (movimientos.isNotEmpty()) {
                        csvContent = viewModel.generarCsvString(movimientos)
                        createDocumentLauncher.launch("Report_$mesFiltro.csv")
                    } else {
                        Toast.makeText(context,
                            context.getString(R.string.no_hay_datos_para_exportar), Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = movimientos.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    stringResource(R.string.generar_y_guardar_csv),
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

    }
}

// --- FUNCIONES DE SOPORTE PARA PDF ---

fun guardarPdfEnUri(context: Context, sourceBitmap: Bitmap, uri: Uri) {


    if (sourceBitmap.width <= 1 || sourceBitmap.height <= 1) {
        // Si el bitmap es diminuto, algo salió mal en la captura
        return
    }
    // CLAVE: Convertir Hardware Bitmap a Software Bitmap
    val softwareBitmap = if (sourceBitmap.config == Bitmap.Config.HARDWARE) {
        sourceBitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        sourceBitmap
    }

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(
        softwareBitmap.width,
        softwareBitmap.height,
        1
    ).create()

    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Dibujamos sobre el canvas del PDF (que solo acepta Software Bitmaps)
    canvas.drawBitmap(softwareBitmap, 0f, 0f, null)
    pdfDocument.finishPage(page)

    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            pdfDocument.writeTo(outputStream)
            outputStream.flush()
        }
        Toast.makeText(context,
            context.getString(R.string.pdf_generado_con_xito), Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context,
            context.getString(R.string.error_al_escribir_archivo), Toast.LENGTH_SHORT).show()
    } finally {
        pdfDocument.close()
        // Liberamos memoria si creamos una copia
        if (softwareBitmap != sourceBitmap) {
            softwareBitmap.recycle()
        }
    }
}

@Composable
fun SelectorMesAnio(
    mesAnioActual: String, // Recibe el formato "MM-yyyy"
    onMesSeleccionado: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    // Separamos el string actual para inicializar los estados
    val partes = mesAnioActual.split("-")
    var mesTemp by remember { mutableStateOf(partes.getOrElse(0) { "02" }) }
    var anioTemp by remember { mutableStateOf(partes.getOrElse(1) { "2026" }) }

    val meses = listOf("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12")
    val anios = listOf("2024", "2025", "2026", "2027", "2028")

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedButton(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.DateRange, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(text = stringResource(R.string.periodo, mesAnioActual))
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(stringResource(R.string.seleccionar_mes_y_año)) },
                text = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Columna de Meses
                        ListaSelector(
                            label = stringResource(R.string.mes),
                            opciones = meses,
                            seleccionado = mesTemp,
                            onOptionSelected = { mesTemp = it }
                        )

                        // Columna de Años
                        ListaSelector(
                            label = stringResource(R.string.año),
                            opciones = anios,
                            seleccionado = anioTemp,
                            onOptionSelected = { anioTemp = it }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        onMesSeleccionado("$mesTemp-$anioTemp")
                        showDialog = false
                    }) {
                        Text(stringResource(R.string.aceptar), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text(stringResource(R.string.cancelar))
                    }
                }
            )
        }
    }
}

@Composable
fun ListaSelector(
    label: String,
    opciones: List<String>,
    seleccionado: String,
    onOptionSelected: (String) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.width(80.dp),
            contentPadding = PaddingValues(vertical = 60.dp) // Para que el centro sea más fácil de seleccionar
        ) {
            items(opciones) { opcion ->
                val isSelected = opcion == seleccionado
                Text(
                    text = opcion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOptionSelected(opcion) }
                        .padding(vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = if (isSelected) 22.sp else 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    ),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun VistaPreviaCSV(movimientos: List<Any>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(stringResource(R.string.vista_previa_del_csv), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Encabezados
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(R.string.tipo), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = MaterialTheme.colorScheme.onBackground)
                Text(stringResource(R.string.cat_csv), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = MaterialTheme.colorScheme.onBackground)
                Text(stringResource(R.string.Monto_csv), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = MaterialTheme.colorScheme.onBackground)
                Text(stringResource(R.string.fecha_csv), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = MaterialTheme.colorScheme.onBackground)
                Text(stringResource(R.string.nota_csv), modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = MaterialTheme.colorScheme.onBackground)
            }

            LazyColumn {
                items(movimientos) { mov ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Usamos una lógica más segura para extraer los datos
                        val tipo: String
                        val cat: String
                        val monto: String
                        val fecha: String
                        val nota: String

                        when (mov) {
                            is IncomeEntity -> {
                                tipo = "Ingreso"
                                cat = mov.categoria
                                monto = mov.cantidad.toString()
                                fecha = mov.fecha
                                nota = mov.nota ?: "" // Si es null, ponemos texto vacío
                            }
                            is ExpenseEntity -> {
                                tipo = "Gasto"
                                cat = mov.categoria
                                monto = mov.cantidad.toString()
                                fecha = mov.fecha
                                nota = mov.nota ?: "" // Si es null, ponemos texto vacío
                            }
                            else -> {
                                tipo = ""; cat = ""; monto = ""; fecha = ""; nota = ""
                            }
                        }

                        // Renderizado de las columnas
                        Text(tipo, modifier = Modifier.weight(1f), fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
                        Text(cat, modifier = Modifier.weight(1f), fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
                        Text("$$monto", modifier = Modifier.weight(1f), fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
                        Text(fecha, modifier = Modifier.weight(1.2f), fontSize = 10.sp, color = MaterialTheme.colorScheme.onBackground)
                        // La nota suele ser más larga, así que le damos un poco más de peso o scroll
                        Text(
                            text = nota,
                            modifier = Modifier.weight(1.5f),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1 // Evita que una nota larga rompa el diseño de la fila
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("LocalContextConfigurationRead")
@Composable
fun ContenidoPDF(
    mesFiltro: String,
    ingresosTotales: Double,
    gastosTotales: Double,
    ingresosPorMes: Map<String, Double>,
    gastosPorCategoria: Map<String, Double>,
    comparacionMensual: List<Triple<String, Double, Double>>,
    totalIngresoVsGasto: Pair<Double, Double>,
    isDarkTheme: Boolean = false
) {
    val context = LocalContext.current
    val anio = mesFiltro.split("-").getOrElse(1) { "2026" }
    val locale = ConfigurationCompat.getLocales(context.resources.configuration)[0]
        ?: Locale.getDefault()

    val isSpanish = locale.language == "es"
    val pattern = if (isSpanish) "dd 'de' MMMM 'de' yyyy" else "MMMM dd, yyyy"

    val fechaGeneracion = LocalDate.now().format(
        DateTimeFormatter.ofPattern(pattern, locale)
    )

    // Colores que cambian según el tema
    val fondoColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textoColor = if (isDarkTheme) Color.White else Color.Black
    val textoSecundario = if (isDarkTheme) Color.LightGray else Color.Gray
    val bordeColor = if (isDarkTheme) Color(0xFF3A3A3A) else Color.LightGray

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(fondoColor)
    ) {
        // ── HEADER ──
        Box(modifier = Modifier.fillMaxWidth()) {
            // Esquinas decorativas
            EsquinaDecorativa(Modifier.align(Alignment.TopStart))
            EsquinaDecorativa(
                Modifier
                    .align(Alignment.TopEnd)
                    .rotate(90f)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.reporte_financiero, anio),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.analisis_de_ingresos_y_gastos),
                    fontSize = 12.sp,
                    color = textoSecundario,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = bordeColor)
        Spacer(modifier = Modifier.height(16.dp))

        // ── RESUMEN TOTAL ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TarjetaResumenPDF(
                titulo = stringResource(R.string.total_ingresos),
                monto = ingresosTotales.toInt().toString(),
                color = Color(0xFF2ECC71),
                textoColor = textoColor,
                bordeColor = bordeColor,
                modifier = Modifier.weight(1f)
            )
            TarjetaResumenPDF(
                titulo = stringResource(R.string.total_gastos),
                monto = gastosTotales.toInt().toString(),
                color = Color(0xFFE74C3C),
                textoColor = textoColor,
                bordeColor = bordeColor,
                modifier = Modifier.weight(1f)
            )
            TarjetaResumenPDF(
                titulo = stringResource(R.string.balance_del_mes),
                monto = (ingresosTotales - gastosTotales).toInt().toString(),
                color = Color(0xFF3498DB),
                textoColor = textoColor,
                bordeColor = bordeColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── GRÁFICA DE BARRAS — INGRESOS POR MES ──
        if (ingresosPorMes.isNotEmpty()) {
            Text(
                text = stringResource(R.string.grafica_analisis_ingresos),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textoColor,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            val nombresMeses = listOf("Ene","Feb","Mar","Abr","May","Jun",
                "Jul","Ago","Sep","Oct","Nov","Dic")
            val valoresMeses = (1..12).map { mes ->
                ingresosPorMes[mes.toString().padStart(2, '0')] ?: 0.0
            }
            val maxValor = valoresMeses.maxOrNull()?.takeIf { it > 0 } ?: 1.0

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, bordeColor, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                val alturaMaxBarra = 100.dp

                Column {
                    Text(
                        text = stringResource(R.string.ingresos),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textoColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        valoresMeses.forEachIndexed { index, valor ->
                            val fraccion = if (maxValor > 0) (valor / maxValor).toFloat() else 0f
                            val alturaReal = alturaMaxBarra * fraccion.coerceAtLeast(0.02f)

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Bottom,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(modifier = Modifier.height(alturaMaxBarra), contentAlignment = Alignment.BottomCenter) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(0.6f)
                                            .height(alturaReal)
                                            .background(
                                                Color(0xFF2980B9),
                                                RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                                            )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = nombresMeses[index],
                                    fontSize = 7.sp,
                                    color = textoColor,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Text(
                text = stringResource(R.string.enero_diciembre, anio),
                fontSize = 10.sp,
                color = textoColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 4.dp),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ── GRÁFICA INGRESOS VS GASTOS (DONUT) ──
        if (totalIngresoVsGasto.first > 0 || totalIngresoVsGasto.second > 0) {
            Text(
                text = stringResource(R.string.ingresos_vs_gastos),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textoColor,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, bordeColor, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                GraficaDonutPDF(
                    ingresos = totalIngresoVsGasto.first,
                    gastos = totalIngresoVsGasto.second
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ── GRÁFICA DE TORTA — GASTOS POR CATEGORÍA ──
        // ... igual que antes ...

        Spacer(modifier = Modifier.height(20.dp))

        // ── COMPARACIÓN MENSUAL (BARRAS AGRUPADAS) ──
        val hayComparacion = comparacionMensual.any { it.second > 0 || it.third > 0 }
        if (hayComparacion) {
            Text(
                text = stringResource(R.string.comparacion_mensual),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textoColor,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, bordeColor, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                GraficaBarrasAgrupadasPDF(comparacionMensual = comparacionMensual)
            }

            Text(
                text = "(${mesFiltro.split("-").getOrElse(1) { "2026" }})",
                fontSize = 10.sp,
                color = textoColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 4.dp),
                textAlign = TextAlign.End
            )
        }

        // ── GRÁFICA DE TORTA — GASTOS POR CATEGORÍA ──
        if (gastosPorCategoria.isNotEmpty()) {
            Text(
                text = stringResource(R.string.grafica_analisis_gastos),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textoColor,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .border(1.dp, bordeColor, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.gastos),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textoColor,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GraficaTortaPDF(gastosPorCategoria = gastosPorCategoria)
                }
            }

            Text(
                text = "(${mesFiltro.replace("-", " - ")})",
                fontSize = 10.sp,
                color = textoColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 4.dp),
                textAlign = TextAlign.End
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        HorizontalDivider(color = Color.LightGray)

        // ── FOOTER ──
        Box(modifier = Modifier.fillMaxWidth()) {
            EsquinaDecorativa(
                Modifier
                    .align(Alignment.BottomStart)
                    .rotate(270f)
            )
            EsquinaDecorativa(
                Modifier
                    .align(Alignment.BottomEnd)
                    .rotate(180f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = stringResource(R.string.generado_por_clearcounts),
                    fontSize = 10.sp,
                    color = textoSecundario,
                    modifier = Modifier.padding(end = 16.dp)
                )
                Text(
                    text = stringResource(R.string.fecha_exportacion, fechaGeneracion),
                    fontSize = 10.sp,
                    color = textoSecundario,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}

@Composable
fun EsquinaDecorativa(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier.size(24.dp)
    ) {
        val stroke = Stroke(width = 2.dp.toPx())
        val color = Color(0xFF3498DB)
        drawLine(color, Offset(0f, 0f), Offset(size.width * 0.6f, 0f), stroke.width)
        drawLine(color, Offset(0f, 0f), Offset(0f, size.height * 0.6f), stroke.width)
    }
}

@Composable
fun TarjetaResumenPDF(
    titulo: String,
    monto: String,
    color: Color,
    textoColor: Color = Color.Black,
    bordeColor: Color = Color.LightGray,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = titulo, fontSize = 9.sp, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = monto,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = color,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun GraficaTortaPDF(gastosPorCategoria: Map<String, Double>) {
    val context = LocalContext.current
    val colores = listOf(
        Color(0xFF2980B9), Color(0xFF27AE60), Color(0xFFE74C3C),
        Color(0xFFF39C12), Color(0xFF9B59B6), Color(0xFF1ABC9C),
        Color(0xFFE67E22), Color(0xFF34495E)
    )
    val total = gastosPorCategoria.values.sum()
    val entradas = gastosPorCategoria.entries.toList()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            var anguloInicio = -90f
            entradas.forEachIndexed { index, (_, valor) ->
                val angulo = (valor / total * 360f).toFloat()
                drawArc(
                    color = colores[index % colores.size],
                    startAngle = anguloInicio,
                    sweepAngle = angulo,
                    useCenter = true
                )
                anguloInicio += angulo
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            entradas.forEachIndexed { index, (categoria, valor) ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .background(colores[index % colores.size], CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${context.traducirCategoria(categoria)} (${((valor / total) * 100).toInt()}%)",
                        fontSize = 9.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}


@Composable
fun GraficaDonutPDF(ingresos: Double, gastos: Double) {
    val total = ingresos + gastos
    val colores = listOf(Color(0xFF2ECC71), Color(0xFFE74C3C))
    val valores = listOf(ingresos, gastos)
    val etiquetas = listOf(stringResource(R.string.ingresos), stringResource(R.string.gastos))

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        // Donut
        Canvas(modifier = Modifier.size(120.dp)) {
            var anguloInicio = -90f
            valores.forEachIndexed { index, valor ->
                val angulo = if (total > 0) (valor / total * 360f).toFloat() else 180f
                drawArc(
                    color = colores[index],
                    startAngle = anguloInicio,
                    sweepAngle = angulo,
                    useCenter = false,
                    style = Stroke(width = 28.dp.toPx(), cap = StrokeCap.Butt)
                )
                anguloInicio += angulo
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Leyenda
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            valores.forEachIndexed { index, valor ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(colores[index], CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = etiquetas[index],
                            fontSize = 11.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${valor.toInt()}",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "${if (total > 0) ((valor / total) * 100).toInt() else 0}%",
                            fontSize = 10.sp,
                            color = colores[index],
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GraficaBarrasAgrupadasPDF(
    comparacionMensual: List<Triple<String, Double, Double>>
) {
    val nombresMeses = listOf("Ene","Feb","Mar","Abr","May","Jun",
        "Jul","Ago","Sep","Oct","Nov","Dic")
    val alturaMaxBarra = 80.dp
    val maxValor = comparacionMensual.flatMap { listOf(it.second, it.third) }
        .maxOrNull()?.takeIf { it > 0 } ?: 1.0

    Column {
        Text(
            text = stringResource(R.string.comparacion_mensual),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            comparacionMensual.forEachIndexed { index, (mes, ingreso, gasto) ->
                val fraccionIngreso = (ingreso / maxValor).toFloat().coerceAtLeast(0.02f)
                val fraccionGasto = (gasto / maxValor).toFloat().coerceAtLeast(0.02f)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier.height(alturaMaxBarra),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(1.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            // Barra ingreso
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(alturaMaxBarra * fraccionIngreso)
                                    .background(
                                        Color(0xFF2ECC71),
                                        RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                    )
                            )
                            // Barra gasto
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(alturaMaxBarra * fraccionGasto)
                                    .background(
                                        Color(0xFFE74C3C),
                                        RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp)
                                    )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = nombresMeses.getOrElse(index) { mes },
                        fontSize = 6.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Leyenda
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(8.dp)
                .background(Color(0xFF2ECC71), CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(R.string.ingresos), fontSize = 9.sp, color = Color.Black)
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier
                .size(8.dp)
                .background(Color(0xFFE74C3C), CircleShape))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = stringResource(R.string.gastos), fontSize = 9.sp, color = Color.Black)
        }
    }
}