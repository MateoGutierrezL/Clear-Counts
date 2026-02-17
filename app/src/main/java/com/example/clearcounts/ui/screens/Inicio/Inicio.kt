package com.example.clearcounts.ui.screens.Inicio

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.clearcounts.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(paddingValues: PaddingValues) {

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
            item {
                GraficoBarras(
                    datos = listOf(2f,3f,4f)
                )
            }
            item {
                Transacciones() // Este contiene toda la parte de movimientos recientes
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
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
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
fun Transacciones() {

    Row() {
        Text(
            text = "Transacciones"
        )
    }
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .clip(RoundedCornerShape(10.dp))
            .width(382.dp),
        ) {
        Box(
            modifier = Modifier
                // CAMBIO: Fondo del contenedor a secondaryContainer
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .width(382.dp),

                ) {
                Text(
                    text = "Movimientos recientes",
                    textAlign = TextAlign.Start,
                    // CAMBIO: Color del texto a onSecondaryContainer
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textDecoration = TextDecoration.Underline
                )

                Row {
                    Text(
                        text = "10/09/2005",
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(10.dp),
                        // CAMBIO: Color del texto a onSecondaryContainer
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = buildAnnotatedString {
                            // Gastos (Rojo)
                            withStyle(style = SpanStyle(Color.Red)) {
                                append("$340.000")
                            }
                            // Separador (Blanco, cambiado a onSecondaryContainer)
                            withStyle(style = SpanStyle(MaterialTheme.colorScheme.onSecondaryContainer)) {
                                append("/")
                            }
                            // Ingresos (Verde)
                            withStyle(style = SpanStyle(Color.Green)) {
                                append("$500.000")
                            }
                        }
                    )
                }

                // --- MOVIMIENTOS INDIVIDUALES ---

                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ingresos),
                        contentDescription = "Icono de la bolsa de monedas",
                        modifier = Modifier
                            .size(90.dp)
                        // CAMBIO: Agregar ColorFilter para que la imagen se adapte si es un Vector
                        // .colorFilter(ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer))
                    )
                    Text(
                        text = "Ingresos extra",
                        // CAMBIO: Color del texto a onSecondaryContainer
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 7.dp)
                    )
                    Text(
                        text = "+$500.000",
                        color = Color.Green, // Color fijo para montos positivos
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    )
                }

                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.gasolina),
                        contentDescription = "Icono de la gasolina",
                        modifier = Modifier
                            .size(90.dp)
                        // CAMBIO: Agregar ColorFilter
                        // .colorFilter(ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer))
                    )
                    Text(
                        text = "Gasolina",
                        // CAMBIO: Color del texto a onSecondaryContainer
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 7.dp)

                    )
                    Text(
                        text = "-$230.000",
                        color = Color.Red, // Color fijo para montos negativos
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    )
                }

                Row(modifier = Modifier.padding(top = 20.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.administracion),
                        contentDescription = "Icono de la administracion",
                        modifier = Modifier
                            .size(90.dp)
                        // CAMBIO: Agregar ColorFilter
                        // .colorFilter(ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer))
                    )
                    Text(
                        text = "Administracion",
                        // CAMBIO: Color del texto a onSecondaryContainer
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 7.dp)

                    )
                    Text(
                        text = "-$110.400",
                        color = Color.Red, // Color fijo para montos negativos
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun contenidoAbajo(){

}