package com.example.clearcounts.ui.screens.Exportar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.ui.screens.Graficas
import com.example.clearcounts.ui.screens.Inicio.GraficoLineChart
import com.example.clearcounts.ui.screens.Inicio.ItemFinanciero
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



//
@Composable
fun PantallaExportarGrafico(viewModel: ExportarGraficosViewModel = hiltViewModel()) {
    val ingresosTotales by viewModel.totalIngresosMensuales.collectAsState()
    val gastosTotales by viewModel.totalGastosMensuales.collectAsState()
    val mesFiltro by viewModel.mesSeleccionado.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        uri?.let { seleccionadoUri ->
            coroutineScope.launch {
                try {
                    // Damos un respiro para que el record() del layer termine
                    delay(150)

                    // 1. Capturamos el bitmap (inicialmente será Hardware Bitmap)
                    val hardwareBitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()

                    // 2. Guardamos pasando por la conversión de software
                    guardarPdfEnUri(context, hardwareBitmap, seleccionadoUri)
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
            Toast.makeText(context, "Guardado con éxito", Toast.LENGTH_SHORT).show()
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
            text = "Vista Previa del documento",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        if (pdf){
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .background(Color.White) // Fondo blanco para evitar transparencia negra en PDF
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),

                    ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Resumen de Movimientos: $mesFiltro",
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier =
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        ItemFinanciero(
                            titulo = "Ingresos",
                            monto = "$ingresosTotales",
                            icono = Icons.AutoMirrored.Default.TrendingUp,
                            modifier = Modifier.weight(1f),
                            tint = Color(0xFF2ECC71)
                        )

                        ItemFinanciero(
                            titulo = "Gastos",
                            monto = "$gastosTotales",
                            icono = Icons.AutoMirrored.Default.TrendingDown,
                            modifier = Modifier.weight(1f),
                            tint = Color(0xFFE74C3C)
                        )
                    }
                    GraficoLineChart()
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
        Spacer(modifier = Modifier.height(7.dp))

        if (pdf){
            Button(
                onClick = { launcher.launch("Reporte_${System.currentTimeMillis()}.pdf") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Guardar PDF", color = Color.White)
            }
        }else{
            Button(
                onClick = {
                    if (movimientos.isNotEmpty()) {
                        csvContent = viewModel.generarCsvString(movimientos)
                        createDocumentLauncher.launch("Reporte_$mesFiltro.csv")
                    } else {
                        Toast.makeText(context, "No hay datos para exportar", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = movimientos.isNotEmpty()
            ) {
                Text("Generar y Guardar CSV")
            }
        }

    }
}

@Composable
fun ContenidoGrafico() {
    Graficas()
}

// --- FUNCIONES DE SOPORTE PARA PDF ---

fun guardarPdfEnUri(context: Context, sourceBitmap: Bitmap, uri: Uri) {
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
        Toast.makeText(context, "¡PDF generado con éxito!", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error al escribir archivo", Toast.LENGTH_SHORT).show()
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
            Text(text = "Periodo: $mesAnioActual")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Seleccionar Mes y Año") },
                text = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Columna de Meses
                        ListaSelector(
                            label = "Mes",
                            opciones = meses,
                            seleccionado = mesTemp,
                            onOptionSelected = { mesTemp = it }
                        )

                        // Columna de Años
                        ListaSelector(
                            label = "Año",
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
                        Text("Aceptar", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancelar")
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
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Black
                    ),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Vista Previa del CSV", fontWeight = FontWeight.Bold, color = Color.Black)
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Encabezados
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Tipo", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = Color.Black)
                Text("Cat.", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = Color.Black)
                Text("Monto", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = Color.Black)
                Text("Fecha", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = Color.Black)
                Text("Nota", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp,color = Color.Black)
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
                        Text(tipo, modifier = Modifier.weight(1f), fontSize = 10.sp, color = Color.Black)
                        Text(cat, modifier = Modifier.weight(1f), fontSize = 10.sp, color = Color.Black)
                        Text("$$monto", modifier = Modifier.weight(1f), fontSize = 10.sp, color = Color.Black)
                        Text(fecha, modifier = Modifier.weight(1.2f), fontSize = 10.sp, color = Color.Black)
                        // La nota suele ser más larga, así que le damos un poco más de peso o scroll
                        Text(
                            text = nota,
                            modifier = Modifier.weight(1.5f),
                            fontSize = 10.sp,
                            color = Color.Black,
                            maxLines = 1 // Evita que una nota larga rompa el diseño de la fila
                        )
                    }
                }
            }
        }
    }
}
