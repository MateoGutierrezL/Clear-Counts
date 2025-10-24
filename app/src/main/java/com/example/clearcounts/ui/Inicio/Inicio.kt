package com.example.clearcounts.ui.Inicio

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.autofill.contentType
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.R
import com.example.clearcounts.ui.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.Barras.DrawerItem
import com.example.clearcounts.ui.Barras.NavigationDrawer
import com.example.clearcounts.ui.Barras.TopBar
import com.example.clearcounts.ui.Graficas
import com.example.clearcounts.ui.Pantallas
import com.example.clearcounts.ui.Perfil.EditarPerfil
import com.example.clearcounts.ui.Perfil.Perfil
import com.example.clearcounts.ui.Presupuesto
import com.example.clearcounts.ui.theme.AzulBotones
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.AzulOscuro
import com.example.clearcounts.ui.theme.Rojo
import com.example.clearcounts.ui.theme.blanco
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.ui.theme.grisClaro
import com.example.clearcounts.ui.theme.negro
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            contenidoSuperior() // Este contiene la barra de progreso hasta la imagen de la mascota
        }
        item {
            contenidoMedio() // Este contiene toda la parte de movimientos recientes
        }
        item { 
            contenidoAbajo() // Este contiene la parte de metas
        }
    }
}


@Composable
fun contenidoSuperior() {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3300, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .clip(RoundedCornerShape(10.dp))
            .width(382.dp)
            .height(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .background(AzulEncabezado)
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 30.dp),
                    text = "¡Vas muy bien!",
                    style = MaterialTheme.typography.titleMedium,
                    color = blanco,
                    textAlign = TextAlign.Center

                )
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .padding(start = 30.dp, end = 30.dp)
                        .border(BorderStroke(1.dp, negro)),
                    color = negro,
                    trackColor = AzulBotones,
                    strokeCap = StrokeCap.Butt,
                )
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = negro,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.perro_feliz),
            contentDescription = "Logo de Clear Counts",
            modifier = Modifier
                .size(200.dp),
            alignment = Alignment.Center
        )
    }

}

@Preview
@Composable
fun contenidoMedio() {

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 30.dp)
            .clip(RoundedCornerShape(10.dp))
            .width(382.dp),

        ) {
        Box(
            modifier = Modifier
                .background(AzulEncabezado)
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
                    color = blanco,
                    textDecoration = TextDecoration.Underline
                )

                Row {
                    Text(
                        text = "10/09/2005",
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(10.dp),
                        color = blanco
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
                            withStyle(style = SpanStyle(Color.White)) {
                                append("/")
                            }
                            withStyle(style = SpanStyle(Color.Green)) {
                                append("$500.000")
                            }
                        }
                    )
                }
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ingresos),
                        contentDescription = "Icono de la bolsa de monedas",
                        modifier = Modifier
                            .size(90.dp)
                    )
                    Text(
                        text = "Ingresos extra",
                        color = blanco,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 7.dp)
                    )
                    Text(
                        text = "+$500.000",
                        color = Color.Green,
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
                    )
                    Text(
                        text = "Gasolina",
                        color = blanco,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 7.dp)

                    )
                    Text(
                        text = "-$230.000",
                        color = Color.Red,
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
                    )
                    Text(
                        text = "Administracion",
                        color = blanco,
                        modifier = Modifier
                            .padding(top = 30.dp, start = 7.dp)

                    )
                    Text(
                        text = "-$110.400",
                        color = Color.Red,
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










