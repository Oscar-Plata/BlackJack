 package com.argent.blackjack

import android.app.Activity
import android.content.Intent
import android.content.res.Resources.Theme
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.argent.blackjack.ui.theme.BlackJackTheme
import com.argent.blackjack.ui.theme.TemaColor

 class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tinyDB : TinyDB = TinyDB(applicationContext)
        var tema=tinyDB.getInt("temaID")
        val tc=TemaColor()
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
                    color = colorResource(R.color.BlackCSTM)
                ) {
                    MainScreen(tc,tema)
                }
            }
        }
    }
  }

@Composable
fun MainScreen(tc:TemaColor,color:Int) {

    val context = LocalContext.current
    val mp =MediaPlayer.create(context,R.raw.tap)
    val haptic = LocalHapticFeedback.current
    var valorMeta by remember{ mutableStateOf(21) }
    var cID by remember { mutableStateOf(color)}
    var c=when(cID){
        1 -> tc.tema1
        2 -> tc.tema2
        else -> tc.tema3
    }

    val startConfigResult=rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()){ respuesta: ActivityResult ->
        if (respuesta.resultCode == Activity.RESULT_OK) {
            val valor = respuesta.data?.getIntExtra("valor",21)
            val tema = respuesta.data?.getIntExtra("tema",0)
            //Toast.makeText(context,"fue $valor", Toast.LENGTH_SHORT).show()
            if (valor != null) {
                valorMeta=valor
            }
            if(tema!=null){
               // Toast.makeText(context,tema.toString(), Toast.LENGTH_SHORT).show()
                cID=tema
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()){

        Box(
            Modifier
                .fillMaxWidth()
                .height(300.dp)){
            Image(painter = painterResource(R.drawable.banner), contentDescription = null, modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(top = 80.dp),
                colorFilter = ColorFilter.tint(colorResource(id = c[1])),contentScale = ContentScale.FillBounds)
            Row(Modifier.padding(top=120.dp), horizontalArrangement = Arrangement.Center) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.c10),
                    contentDescription = null,
                    modifier = Modifier.heightIn(80.dp, 140.dp)
                )
                Image(
                    painter = painterResource(R.drawable.ha),
                    contentDescription = null,
                    modifier = Modifier.heightIn(80.dp, 140.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            BarraTitulo(titulo = "BLACKJACK!", tamano = 110, color = colorResource(id = c[0]))
        }

        
        Spacer(modifier = Modifier.height(50.dp))
        //BOTON PARA ABRIR LA PANTALLA DE JUEGO
        Button(onClick = {
            if(mp.isPlaying)mp.pause()
            mp.start()

            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            val intentGame = Intent(context, GameActivity::class.java)
            intentGame.putExtra("meta",valorMeta)
            context.startActivity(intentGame) },
            modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 20.dp)
            .clip(CircleShape)
            .height(100.dp),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[2]))
        ) {
            Text(text = "Jugar",style= MaterialTheme.typography.body2,fontSize = 40.sp,color = colorResource(R.color.WhiteCSTM))
            Spacer(modifier = Modifier.widthIn(20.dp,150.dp))
            Image(painter = painterResource(R.drawable.play), contentDescription = null, modifier = Modifier.heightIn(20.dp,100.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))

        //BOTON PARA ABRIR LA PANTALLA DE PUNTUACIONES
        Button(onClick = {
            if(mp.isPlaying)mp.pause()
            mp.start()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            val intentScores = Intent(context, ScoresActivity::class.java)
            context.startActivity(intentScores) },modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 20.dp)
            .clip(CircleShape)
            .height(100.dp),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[3]))) {
            Text(text = "Scores",style= MaterialTheme.typography.body2,fontSize = 40.sp,color = colorResource(R.color.WhiteCSTM))
            Spacer(modifier = Modifier.widthIn(10.dp,150.dp))
            Image(painter = painterResource(R.drawable.poker), contentDescription = null, modifier = Modifier.heightIn(20.dp,100.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row {

            //BOTON PARA ABRIR LA PANTALLA DE REGLAS
            Button(onClick = {
                if(mp.isPlaying)mp.pause()
                mp.start()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val intentInfo = Intent(context, InfoActivity::class.java)
                context.startActivity(intentInfo)
                             },modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 5.dp, bottom = 5.dp, end = 5.dp)
                .clip(CircleShape)
                .weight(1f)
                .height(100.dp),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[3]))) {
                Text(text = "Reglas",style= MaterialTheme.typography.body2,color = colorResource(R.color.WhiteCSTM))
                Image(painter = painterResource(R.drawable.rule), contentDescription = null, modifier = Modifier.heightIn(20.dp,50.dp))
            }

            //BOTON PARA ABRIR LA PANTALLA DE AJUSTES
            Button(onClick = {
                if(mp.isPlaying)mp.pause()
                mp.start()
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                val intentConfig = Intent(context, ConfigActivity::class.java)
                intentConfig.putExtra("valorM",valorMeta)
                startConfigResult.launch(intentConfig)
                             },modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, top = 5.dp, bottom = 5.dp, end = 20.dp)
                .clip(CircleShape)
                .weight(1f)
                .height(100.dp),colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(c[3]))) {
                Text(text = "Ajustes",style= MaterialTheme.typography.body2,color = colorResource(R.color.WhiteCSTM))
                Image(painter = painterResource(R.drawable.gears), contentDescription = null, modifier = Modifier.heightIn(20.dp,50.dp),
                    )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(Modifier.padding(horizontal = 10.dp, vertical = 0.dp)){
            Text(text = "Version: 1.0",style= MaterialTheme.typography.body2,fontSize = 12.sp,color = colorResource(R.color.WhiteCSTM))
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "@OscarArgent",style= MaterialTheme.typography.body2,fontSize = 12.sp,color = colorResource(R.color.WhiteCSTM))
        }

    }

}

 @Composable
 fun BarraTituloBoton(titulo: String,tamano: Int,color:Color,colorb:Color){
     val context = LocalContext.current as? Activity
     val mp=MediaPlayer.create(context,R.raw.tap)
     val haptic = LocalHapticFeedback.current
     Box(modifier = Modifier
         .fillMaxWidth()
         .height(tamano.dp)){
         Image(painter = painterResource(R.drawable.banner), contentDescription = null, modifier = Modifier
             .height(tamano.dp)
             .fillMaxWidth(),
             colorFilter = ColorFilter.tint(color),contentScale = ContentScale.FillBounds)
         Row(
             Modifier
                 .padding(vertical = 10.dp, horizontal = 20.dp)
                 .wrapContentHeight(align = Alignment.CenterVertically)){
             Button(onClick = {
                 if(mp.isPlaying) mp.pause()
                 mp.start()
                 haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                 context?.finish()
                              },modifier= Modifier
                 .clip(CircleShape)
                 .height((tamano / 1.8).dp),colors = ButtonDefaults.buttonColors(backgroundColor = colorb)) {
                 Text(text = "<",style= MaterialTheme.typography.body2,fontSize = (tamano/3.5).sp,color = colorResource(R.color.WhiteCSTM))
             }
             Spacer(modifier = Modifier.weight(1f))
             Text(text = titulo,style= MaterialTheme.typography.body2,fontSize = (tamano/3).sp,textAlign = TextAlign.Center, color =  colorResource(id = R.color.WhiteCSTM),
                 modifier= Modifier.fillMaxSize())
         }
     }
 }

 @Composable
 fun BarraTitulo(titulo: String,tamano: Int,color: Color){
     Box(modifier = Modifier
         .fillMaxWidth()
         .height(tamano.dp)){
         Image(painter = painterResource(R.drawable.banner), contentDescription = null, modifier = Modifier
             .height(tamano.dp)
             .fillMaxWidth(),
             colorFilter = ColorFilter.tint(color),contentScale = ContentScale.FillBounds)
         Row(Modifier.padding(vertical=10.dp, horizontal = 20.dp)){
             Text(text = titulo,style= MaterialTheme.typography.body2,fontSize = (tamano/3).sp,textAlign = TextAlign.Center, color = colorResource(R.color.WhiteCSTM),
                 modifier= Modifier
                     .fillMaxSize()
                     .wrapContentHeight(align = Alignment.CenterVertically))
         }
     }
 }

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BlackJackTheme {
        val c=TemaColor().tema2
        MainScreen(TemaColor(),1)
    }
}