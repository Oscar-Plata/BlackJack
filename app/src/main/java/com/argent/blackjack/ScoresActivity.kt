package com.argent.blackjack

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.argent.blackjack.ui.theme.BlackJackTheme
import com.argent.blackjack.ui.theme.TemaColor

class ScoresActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tinyDB : TinyDB = TinyDB(applicationContext)
        var tema=tinyDB.getInt("temaID")
        val tc= TemaColor()
        var colores=when(tema){
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
                    val tinyDB : TinyDB = TinyDB(applicationContext)
                    val resultados=tinyDB.getListString("resArray")
                   ScoresScreen(resultados,colores)
                }
            }
        }
    }
}

@Composable
fun ScoresScreen(lista:ArrayList<String>,c:List<Int> ){
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        BarraTituloBoton(titulo = "Puntuaciones", tamano = 100,color= colorResource(c[0]),colorb=colorResource(c[1]))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(20.dp )) {
            items(lista) { item ->
                Marcador(item,c[3])
            }
        }
    }
}

@Composable
fun Marcador(resultado:String,color: Int){
    val con= LocalContext.current
    val datos=resultado.split("|").toTypedArray()
    val stringGanador=when(datos[0].toInt()){
        1 -> "Gano Jugaddor"
        2 -> "Gano Crupier"
        3 -> "Empate Ambos Ganaron"
        4 -> "Empate Ambos Perdieron"
        else -> "Error"
    }
    val strJ=datos[1].replace("[","").replace("]","")
    val strC=datos[2].replace("[","").replace("]","")
    val cartasJ =strJ.split(",").toTypedArray()
    val cartasC =strC.split(",").toTypedArray()
    val fechaHora =datos[3].split("T").toTypedArray()
    val horaArray=fechaHora[1].split(":").toTypedArray()
    val hora="${horaArray[0]}:${horaArray[1]}"
    Column(Modifier.background(colorResource(id = R.color.BlackCSTM))) {
        BarraTitulo(titulo ="\uD83C\uDFC1${datos[4]}\t\t $stringGanador" , tamano =50 , color = colorResource(color) )
        Row{
           Text(text = "Jugador♦️${datos[5]}",style= MaterialTheme.typography.body2)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier.padding(10.dp)) {
                items(cartasJ) {
                    Carta(x = it.trim())
                }
            }
        }
        Row{
            Text(text = "Crupier♣️${datos[6]}",style= MaterialTheme.typography.body2)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp),modifier = Modifier.padding(10.dp)) {
                items(cartasC) {
                    Carta(x = it.trim())
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .background(color =colorResource(color))){
            Text(text = fechaHora[0],style= MaterialTheme.typography.body2)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = hora,style= MaterialTheme.typography.body2)
        }
    }

}


@Composable
fun Carta(x:String){
    val context = LocalContext.current
    var src=context.resources.getIdentifier(x, "drawable",context.packageName)
    Image(painter = painterResource(id = src), contentDescription = x,modifier = Modifier.heightIn(20.dp, 70.dp))
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    BlackJackTheme {
        val c=TemaColor().tema1

        var jugadoresLista = ArrayList<String>()
        jugadoresLista.add("Oscar,12/03/23,312")
        jugadoresLista.add("Sara,12/03/23,312")
       ScoresScreen(jugadoresLista,c)
    }
}