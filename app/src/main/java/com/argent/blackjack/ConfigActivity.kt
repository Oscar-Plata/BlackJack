package com.argent.blackjack

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.blackjack.ui.theme.BlackJackTheme
import com.argent.blackjack.ui.theme.FredokaOne
import com.argent.blackjack.ui.theme.TemaColor

class ConfigActivity : ComponentActivity() {
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
                    var meta= intent.getIntExtra("valorM",21)
                    ConfigScreen(meta,colores,tema,tinyDB)
                }
            }
        }
    }
}

@Composable
fun ConfigScreen(x:Int,c:List<Int>,t:Int ,tinyDB:TinyDB){
    val mp = MediaPlayer.create(LocalContext.current,R.raw.tap)
    val temaApp= listOf("Escala de Grises","Android Defecto","Original")
    val seleccion= remember { mutableStateOf(temaApp[t])}
    var valor by remember { mutableStateOf(x.toString()) }
    val context = LocalContext.current as? Activity

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
        BarraTituloBoton(titulo = "Ajustes", tamano = 100,color= colorResource(c[0]),colorb=colorResource(c[1]))
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Valor Meta a Jugar",style= MaterialTheme.typography.body2)
        TextField(value = valor, onValueChange = {valor=it},textStyle = TextStyle(color = colorResource(R.color.WhiteCSTM), fontSize =30.sp,  fontFamily = FredokaOne, textAlign = TextAlign.Center), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "Tema del juego",style= MaterialTheme.typography.body2)
        RadioGroup(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
            items = temaApp,selection=seleccion.value,
            onItemClick = {clicked -> seleccion.value=clicked}
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = {
            if(mp.isPlaying)mp.pause()
            mp.start()
            val intent = Intent()
            intent.putExtra("valor", valor.toInt())
            //Toast.makeText(context,"den $valor",Toast.LENGTH_SHORT).show()

            val sel=temaApp.indexOf(seleccion.value)
            tinyDB.putInt("temaID",sel)
            intent.putExtra("tema", sel)
            context?.setResult(Activity.RESULT_OK, intent)
            context?.finish() },
            Modifier
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .fillMaxWidth()
                .height(150.dp)
                .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[2]))
                )

        {
            Text(text = "Guardar",style= MaterialTheme.typography.body2,color= colorResource(id = R.color.WhiteCSTM))
        }
    }
}

@Composable
fun RadioGroup(
    modifier: Modifier,
    items: List<String>,
    selection: String,
    onItemClick: ((String)-> Unit)
){
    Column(modifier = modifier) {
        items.forEach{item ->
            Row {
                RadioButton(selected = item == selection, onClick = { onItemClick(item) })
                Text(text = item,style= MaterialTheme.typography.body2)
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    BlackJackTheme {
        val tinyDB : TinyDB = TinyDB(LocalContext.current)
        val tema=tinyDB.getInt("temaID")
        val tc= TemaColor()
        ConfigScreen(21,tc.tema1,tema,tinyDB)
    }
}