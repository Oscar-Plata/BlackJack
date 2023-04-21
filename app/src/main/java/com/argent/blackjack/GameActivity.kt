package com.argent.blackjack

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.blackjack.ui.theme.BlackJackTheme
import com.argent.blackjack.ui.theme.TemaColor
import java.time.LocalDateTime

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //hola
        val gvm by viewModels<GameViewModel>()
        val tinyDB= TinyDB(applicationContext)
        val tema=tinyDB.getInt("temaID")
        val tc= TemaColor()
        val colores=when(tema){
            1 -> tc.tema1
            2 -> tc.tema2
            else -> tc.tema3
        }
        val valorMeta= intent.getIntExtra("meta",21)
        gvm.setMeta(valorMeta)
        setContent {
            BlackJackTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    GameScreen(valorMeta,gvm,tinyDB,colores)
                }
            }
        }
    }
}

@Composable
fun GameScreen(valorMeta: Int, gameViewModel: GameViewModel,tinyDB: TinyDB,c:List<Int>){
    val context = LocalContext.current
    var mp= MediaPlayer.create(context,R.raw.tap)
    val guis by gameViewModel.estadoUI.collectAsState()
    val bannerText= when(guis.resultado){
        0 -> "Juega ♠️♥️♦️♣️"
        1 -> "Gano Jugador 👑😎"
        2 -> "Gano Crupier 👑🤖"
        3 -> "Ganaron Ambos 👑🤖👑😎"
        4 -> "Perdieron Ambos 😶😥"
        else -> "Error Esto no deberia verse"
    }
    val resArray=tinyDB.getListString("resArray")
    val resString:String
    val puntosC =(if(guis.resultado!=0) guis.puntosCrupier.toString() else "??")
//


    if (guis.resultado!=0){
        if(mp.isPlaying)mp.pause()
        mp=MediaPlayer.create(context,R.raw.win)
        mp.start()
        resString="${guis.resultado}|${guis.cartasJugador}|${guis.cartasCrupier}|${LocalDateTime.now()}|${guis.metaBlackjack}|${guis.puntosJugador}|${guis.puntosCrupier}"
//        Toast.makeText(context,resString,Toast.LENGTH_SHORT).show()
        resArray.add(resString)
        tinyDB.putListString("resArray",resArray)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxWidth()){

            Row(Modifier.background(color = colorResource(id = c[1]))) {
                Text(text = bannerText,modifier= Modifier
                    .padding(top = 110.dp, bottom = 10.dp, start = 20.dp)
                    .weight(1f),style= MaterialTheme.typography.body2, color = colorResource(R.color.WhiteCSTM))
                Button(onClick = {
                    if(mp.isPlaying) mp.pause()
                    mp=MediaPlayer.create(context,R.raw.tap)
                    mp.start()
                    gameViewModel.reset()
                                 gameViewModel.setMeta(valorMeta)}
                    ,Modifier.weight(.5f).padding(top=100.dp,end=10.dp).clip(
                    CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[3]))) {
                    Text(text = "Reset",style= MaterialTheme.typography.body2,color = colorResource(R.color.WhiteCSTM))
                }
            }

            BarraTituloBoton(titulo = "BlackJack : ${guis.metaBlackjack}", tamano = 100, color = colorResource(id = c[0]), colorb =colorResource(id = c[1]) )
        }

        //CRUPIER

        BarraTitulo(titulo = "Crupier\t\t\t\t⭐ $puntosC", tamano = 100,color=colorResource(id = c[1]))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier.padding(10.dp))
        {
            itemsIndexed(guis.cartasCrupier){index,item ->
                    Naipe(carta = item, volteada = guis.estadoCartasCrupier[index], girable = false,onEstadoChange = {gameViewModel.voltearCartaCrupier(index,it)})
            }
        }


        //pLAYER
        BarraTitulo(titulo = "Jugador \t\t\t\t⭐ ${guis.puntosJugador}", tamano = 100,color=colorResource(id = c[4]))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier.padding(10.dp))
        {
            itemsIndexed(guis.cartasJugador){index,item ->
                    Naipe(carta = item, volteada = guis.estadoCartasJugador[index], girable = true, onEstadoChange = {
                        gameViewModel.voltearCartaJugador(index,it)})
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = {
            if(mp.isPlaying)mp.pause()
            mp=MediaPlayer.create(context,R.raw.take)
            mp.start()
            gameViewModel.botonTomar() }
            , modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[2])),enabled = !guis.jugadorPlantado)
        {
            Text(text="Tomar Carta", modifier = Modifier.weight(1f), color=colorResource(R.color.WhiteCSTM), fontSize = 25.sp, style = MaterialTheme.typography.body2)
            Image(painter = painterResource(id = R.drawable.pick), contentDescription = "Tomar Carta",modifier = Modifier
                .heightIn(20.dp, 100.dp)
                .weight(0.5f))
        }
        Button(onClick = {
            if(mp.isPlaying)mp.pause()
            mp=MediaPlayer.create(context,R.raw.set)
            mp.start()
            gameViewModel.botonPlantar() }
            , modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clip(CircleShape),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[3])))
        {
            Text(text="Plantarse", modifier = Modifier.weight(1f), color=colorResource(R.color.WhiteCSTM), fontSize = 25.sp, style = MaterialTheme.typography.body2)
            Image(painter = painterResource(id = R.drawable.poker), contentDescription = "Tomar Carta",modifier = Modifier
                .heightIn(20.dp, 100.dp)
                .weight(0.5f))
        }
    }
}

@Composable
fun Naipe(carta: String,volteada: Boolean,girable:Boolean,onEstadoChange: (Boolean) -> Unit){
    val context = LocalContext.current
    val mp = MediaPlayer.create(context,R.raw.tap)
    var aux =volteada
    val busqueda=when(aux){
        true ->context.resources.getIdentifier(carta, "drawable",context.packageName)
        false->context.resources.getIdentifier("joker", "drawable",context.packageName)
    }

    Image(modifier = Modifier
        .heightIn(20.dp, 100.dp)
        .clickable {
            if (girable) {
                if(mp.isPlaying)mp.pause()
                mp.start()
                aux = !aux
                onEstadoChange(aux)
            }
        },painter = painterResource(busqueda), contentDescription = carta
        )

}
@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    BlackJackTheme {
        val gvm=GameViewModel()
        val tinyDB  = TinyDB(LocalContext.current)
        val c=TemaColor().tema2
        GameScreen(21,gvm,tinyDB,c)
    }
}