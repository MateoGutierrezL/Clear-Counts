package com.example.clearcounts.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado

@Preview
@Composable
fun Graficas() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Gráficos", fontSize = 30.sp, color = AzulEncabezado)
            Spacer(modifier = Modifier.size(20.dp))
            contenido()
        }
    }
}


@Composable
fun contenido() {
    var ingreso by rememberSaveable { mutableStateOf(false) }
    var gasto by rememberSaveable { mutableStateOf(false) }

    val colorTextoIngreso = if (isSystemInDarkTheme()) {
        if (ingreso) Color.Cyan else Color.Black
    } else {
        if (ingreso) Color.Black else Color.Black
    }

    val colorTextoGasto = if (isSystemInDarkTheme()) {
        if (gasto) Color.Cyan else Color.Black
    } else {
        if (gasto) Color.Black else Color.Black
    }
    val colorTexto = if (isSystemInDarkTheme()) Color.White else Color.Black

    Column(

        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .clip(RoundedCornerShape(10.dp))
            .width(382.dp),
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .width(382.dp),
            ) {
                LazyRow(modifier = Modifier.fillMaxWidth()) {
                    item {
                        Button(
                            onClick = { ingreso = true },
                            modifier = Modifier
                                .size(width = 180.dp, height = 64.dp)
                                .padding(top = 15.dp, bottom = 15.dp, end = 7.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (ingreso)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.inversePrimary
                            )
                        ) {
                            Text(
                                text = "Gastos por categoria",
                                fontSize = 13.sp,
                                color = colorTextoIngreso
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .size(width = 180.dp, height = 64.dp)
                                .padding(top = 15.dp, bottom = 15.dp, end = 7.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (ingreso)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.inversePrimary
                            )
                        ) {
                            Text(
                                text = "Ingreso por categoria",
                                fontSize = 11.sp,
                                color = colorTextoIngreso
                            )
                        }
                    }

                    item {
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .size(width = 180.dp, height = 64.dp)
                                .padding(top = 15.dp, bottom = 15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (ingreso)
                                    MaterialTheme.colorScheme.primaryContainer
                                else
                                    MaterialTheme.colorScheme.inversePrimary
                            )
                        ) {
                            Text(
                                text = "Balance Mensual",
                                fontSize = 13.sp,
                                color = colorTextoIngreso
                            )
                        }
                    }
                }

                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                ) {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.flecha),
                            contentDescription = "Flecha Izquierda",
                            modifier = Modifier
                                .size(15.dp)
                                .rotate(180f), tint = colorTexto,
                        )
                    }
                    Text(
                        text = "Noviembre",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 10.dp, end = 35.dp, start = 50.dp), color = colorTexto
                    )

                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(id = R.drawable.flecha),
                            contentDescription = "Flecha Derecha",
                            modifier = Modifier
                                .size(15.dp), tint = colorTexto
                        )
                    }
                }
                CircularProgressBar(percentage = 0.8f, number = 100)

                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.camion),
                        modifier = Modifier
                            .padding(top = 30.dp, start = 20.dp, end = 1.dp)
                            .size(30.dp),
                        contentDescription = "Icono de Transporte"
                    )

                    Text( text = "Transporte",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 35.dp, start = 10.dp),
                    )
                }

                Text(text = "_________________________________")
                Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.corazon),
                        modifier = Modifier
                            .padding(top = 50.dp, start = 20.dp)
                            .size(30.dp),
                        contentDescription = "Icono de Corazon"
                    )

                    Text( text = "Salud",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 49.dp, start = 10.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressBar(
    percentage: Float,
    number: Int,
    fontSize: TextUnit = 28.sp,
    radius: Dp = 50.dp,
    color: Color = AzulEncabezado,
    strokeWidth: Dp = 8.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember { mutableStateOf(false) }

    val curPercentage = animateFloatAsState(
        targetValue = if (animationPlayed) percentage else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )

    LaunchedEffect(true) {
        animationPlayed = true
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(radius * 2f)
    ) {
        Canvas(modifier = Modifier.size(radius * 2f)) {
         drawArc(
             color = color,
             -90f,
             360 * curPercentage.value,
             useCenter = false,
             style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
         )
        }
        Text(
            text =(curPercentage.value * number).toInt().toString(),
            color = Color.Black,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}
