package com.example.clearcounts.ui.screens.Exportar

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.clearcounts.ui.screens.Graficas
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



@Composable
fun PantallaExportarGrafico() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Reporte de Gastos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        // --- ZONA DE CAPTURA ---
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
            ContenidoGrafico()
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = { launcher.launch("Reporte_${System.currentTimeMillis()}.pdf") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Guardar PDF", color = Color.White)
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