package com.example.clearcounts.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado

@Preview
@Composable
fun Presupuesto() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Presupuesto", fontSize = 30.sp, color = AzulEncabezado)
            Text(text = "__________________________________________", color = AzulEncabezado)

            contenidoCentro()
        }
    }
}

@Composable
fun contenidoCentro() {
    val colorTexto = if (isSystemInDarkTheme()) Color.White else Color.Black
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .clip(RoundedCornerShape(10.dp))
            .width(382.dp),

        ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .width(382.dp),

                ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
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
                Row() {
                    Icon(
                        painter = painterResource(id = R.drawable.mensual),
                        contentDescription = "Tuerca con Plata",
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .size(25.dp),
                        tint = colorTexto
                    )
                    Text(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 7.dp, start = 6.dp),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(AzulEncabezado)) {
                                append("Total Mensual\n")
                            }
                            withStyle(style = SpanStyle(colorTexto)) {
                                append("$6.222.000")
                            }
                        }
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.diario),
                        contentDescription = "Sarta de Plata",
                        modifier = Modifier
                            .padding(top = 5.dp, start = 50.dp)
                            .size(30.dp), tint = colorTexto
                    )
                    Text(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 7.dp, start = 15.dp),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(AzulEncabezado)) {
                                append("Uso diario\n")
                            }
                            withStyle(style = SpanStyle(colorTexto)) {
                                append("$1.000.000")
                            }
                        }
                    )
                }

                Text(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 7.dp, start = 25.dp),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(AzulEncabezado)) {
                            append("Restan  ")
                        }
                        withStyle(style = SpanStyle(colorTexto)) {
                            append("$6.105.112.00")
                        }
                    }
                )

                val infiniteTransition = rememberInfiniteTransition(label = "ProgressAnimation")
                val animatedProgress by infiniteTransition.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 3300, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ), label = "animatedProgress"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(start = 20.dp, end = 30.dp, top = 1.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Butt,
                )
                Row() {
                    Icon(
                        painter = painterResource(R.drawable.circulo_relleno),
                        contentDescription = "Circulo Relleno", tint = AzulEncabezado,
                        modifier = Modifier
                            .padding(top = 10.dp, start = 22.dp)
                            .size(15.dp)

                    )
                    Text(
                        text = "$119.888.00 de 6.225.000.00",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 15.dp, top = 5.dp), color = colorTexto
                    )
                }
                Row() {
                    Image(
                        painter = painterResource(R.drawable.camion),
                        modifier = Modifier
                            .padding(top = 30.dp, start = 20.dp)
                            .size(30.dp),
                        contentDescription = "Icono de Transporte"
                    )

                    Text(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 23.dp, start = 10.dp),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = AzulEncabezado)) {
                                append("Transporte\n")
                            }
                            withStyle(style = SpanStyle(AzulEncabezado)) {
                                append("Restan  ")
                            }
                            withStyle(style = SpanStyle(colorTexto)) {
                                append("$6.105.112.00")
                            }
                        }
                    )
                }

                val infiniteTransition2 = rememberInfiniteTransition(label = "ProgressAnimation")
                val animatedProgress2 by infiniteTransition2.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 3300, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ), label = "animatedProgress"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(start = 20.dp, end = 30.dp, top = 1.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Butt,
                )
                Row() {
                    Text(
                        text = "$48.888.00 de 100.000.00",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 50.dp, top = 5.dp), color = colorTexto
                    )
                }
                Row() {
                    Image(
                        painter = painterResource(R.drawable.corazon),
                        modifier = Modifier
                            .padding(top = 30.dp, start = 20.dp)
                            .size(30.dp),
                        contentDescription = "Icono de Corazon"
                    )

                    Text(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 24.dp, start = 10.dp),
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(color = AzulEncabezado)) {
                                append("Salud\n")
                            }
                            withStyle(style = SpanStyle(AzulEncabezado)) {
                                append("Restan  ")
                            }
                            withStyle(style = SpanStyle(colorTexto)) {
                                append("$71.000.00")
                            }
                        }
                    )
                }

                val infiniteTransition3 = rememberInfiniteTransition(label = "ProgressAnimation")
                val animatedProgress3 by infiniteTransition3.animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(durationMillis = 3300, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse,
                    ), label = "animatedProgress"
                )

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(start = 20.dp, end = 30.dp, top = 1.dp)
                        .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimaryContainer)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    strokeCap = StrokeCap.Butt,
                )
                Row() {
                    Text(
                        text = "$0.00 de 71.000.00",
                        fontSize = 10.sp,
                        modifier = Modifier.padding(start = 50.dp, top = 5.dp), color = colorTexto
                    )
                }
            }
        }
    }
}