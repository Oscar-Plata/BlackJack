package com.argent.blackjack

import android.content.ClipData.Item
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.blackjack.ui.theme.BlackJackTheme
import com.argent.blackjack.ui.theme.TemaColor

class InfoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tinyDB : TinyDB = TinyDB(applicationContext)
        val tema=tinyDB.getInt("temaID")
        val tc= TemaColor()
        val colores=when(tema){
            1 -> tc.tema1
            2 -> tc.tema2
            else -> tc.tema3
        }
        setContent {
            BlackJackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    InfoScreen(colores)
                }
            }
        }
    }
}

@Composable
fun InfoScreen(colores: List<Int>) {
    Column() {


        BarraTituloBoton(
            titulo = "♥️Reglas",
            tamano = 100,
            color = colorResource(id = colores[0]),
            colorb = colorResource(
                id = colores[1]
            )
        )
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier.padding(10.dp)
        ) {
            item {
                Text(
                    text = "BlackJack:",
                    style = MaterialTheme.typography.body2,
                    fontSize = 20.sp,
                    color = colorResource(colores[3])
                )
            }
            item {
                Text(
                    text = "♦️El juego consiste en obtener cartas que en su valor numerico sumen 21 (o establecido) sin pasarse.",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♦️Al inicio del juego se repatirán 2 cartas a cada jugador (1 visible y  1 oculta)",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♦️Las cartas numericas otorgaran su valor respectivo en puntos, las cartas J Q K tendran un valor de 10 y las cartas AS pueden valer 1 u 11 segun ell jugador",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♥️Jugador:",
                    style = MaterialTheme.typography.body2,
                    fontSize = 20.sp,
                    color = colorResource(colores[3])
                )
            }
            item {
                Text(
                    text = "♣️En cada turno los jugadores pueden decidir si toman una carta nueva o se quedan 'Plantados' con su mazo actual.",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♣️Todo jugador que se pase del 21 pierde automaticamente.",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♣️Si ambos jugadores no alcanzaron el valor meta (21), gana el jugador con el valor mas cercano.",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♥️Crupier:",
                    style = MaterialTheme.typography.body2,
                    fontSize = 20.sp,
                    color = colorResource(colores[3])
                )
            }
            item {
                Text(
                    text = "♠️El crupier o banca se encarga de repartir las cartas.",
                    style = MaterialTheme.typography.body2,
                    color = colorResource(R.color.WhiteCSTM),
                    fontSize = 15.sp
                )
            }
            item {
                Text(
                    text = "♠️Las cartas del crupier estaran ocultas hasta que todos los jugadores se planten y muestren sus mazos.",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
            item {
                Text(
                    text = "♠️EL curpier esta obligado a tomar cartas y no plantarse si tiene un valor acumulador menor a 17 (4 puntos menos que el valor meta 21).",
                    style = MaterialTheme.typography.body2,
                    fontSize = 15.sp,
                    color = colorResource(R.color.WhiteCSTM)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview5() {
    BlackJackTheme {
        InfoScreen(TemaColor().tema1)
    }
}