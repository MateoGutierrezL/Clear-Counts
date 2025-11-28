package com.example.clearcounts.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.R
import com.example.clearcounts.ui.theme.AzulEncabezado

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
            Text(text = "Graficas", fontSize = 30.sp, color = AzulEncabezado)
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
                                containerColor = Color.Blue
                            )
                        ) {
                            Text(
                                text = "Ingreso por categoria",
                                fontSize = 13.sp
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
                                containerColor = Color.Blue
                            )
                        ) {
                            Text(
                                text = "Balance Mensual",
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Row {
                    Text(
                        text = "10/09/2005",
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(10.dp),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.End,
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(Color.Red)) {
                                append("$340.000")
                            }
                            withStyle(style = SpanStyle(MaterialTheme.colorScheme.onSecondaryContainer)) {
                                append("/")
                            }
                            withStyle(style = SpanStyle(Color.Green)) {
                                append("$500.000")
                            }
                        }
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
    color: Color = Color.Green,
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

        }
    }
}
