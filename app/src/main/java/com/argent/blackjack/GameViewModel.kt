package com.argent.blackjack

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class GameViewModel: ViewModel() {

    val mazo: Set<Carta> = setOf(
        Carta("c10",10),
        Carta("c2",2),
        Carta("c3",3),
        Carta("c4",4),
        Carta("c5",5),
        Carta("c6",6),
        Carta("c7",7),
        Carta("c8",8),
        Carta("c9",9),
        Carta("ca",1),
        Carta("cj",10),
        Carta("ck",10),
        Carta("cq",10),
        Carta("d10",10),
        Carta("d2",2),
        Carta("d3",3),
        Carta("d4",4),
        Carta("d5",5),
        Carta("d6",6),
        Carta("d7",7),
        Carta("d8",8),
        Carta("d9",9),
        Carta("da",1),
        Carta("dj",10),
        Carta("dk",10),
        Carta("dq",10),
        Carta("h10",10),
        Carta("h2",2),
        Carta("h3",3),
        Carta("h4",4),
        Carta("h5",5),
        Carta("h6",6),
        Carta("h7",7),
        Carta("h8",8),
        Carta("h9",9),
        Carta("ha",1),
        Carta("hj",10),
        Carta("hk",10),
        Carta("hq",10),
        Carta("s10",10),
        Carta("s2",2),
        Carta("s3",3),
        Carta("s4",4),
        Carta("s5",5),
        Carta("s6",6),
        Carta("s7",7),
        Carta("s8",8),
        Carta("s9",9),
        Carta("sa",1),
        Carta("sj",10),
        Carta("sk",10),
        Carta("sq",10)

    )
    final val TAMANOMAZO =52
    private val _estadoUI= MutableStateFlow(GameUiState())
    val estadoUI: StateFlow<GameUiState> = _estadoUI.asStateFlow()

    private var cartasUsadas: MutableSet<Carta> = mutableSetOf()

    init {
        reset()
    }

    fun setMeta(meta: Int){
        _estadoUI.update {estadoActual -> estadoActual.copy(metaBlackjack = meta)}
    }


    fun reset(){
        cartasUsadas.clear()
        _estadoUI.value= GameUiState()
        turnoJugador()
        turnoCrupier()
        turnoJugador()
        turnoCrupier()
    }

    fun turnoJugador(){
        var puntos= _estadoUI.value.puntosJugador
        val meta= _estadoUI.value.metaBlackjack
        if(puntos<meta){
            val carta= obtenerCarta()
            var lista=_estadoUI.value.cartasJugador.toMutableList()
            var estados=_estadoUI.value.estadoCartasJugador.toMutableList()
            var contador=_estadoUI.value.contadorJugador
            var asc=_estadoUI.value.asJugador
            estados.add(contador==0)
            contador++
            lista.add(carta.identificador)
            lista.toList()
            estados.toList()
            asc+=(if(carta.valor==1) 1 else 0)

            if(carta.valor==1){
                if( (meta-puntos)>11) puntos+=11
                else puntos+=1
            }else{
                puntos+=carta.valor
            }
            _estadoUI.update {estadoActual ->
                estadoActual.copy(
                    puntosJugador = puntos,
                    cartasJugador = lista,
                    estadoCartasJugador=estados,
                    contadorJugador = contador,
                    asJugador = asc
                )
            }
        }else{
            botonPlantar()
        }
    }

    fun turnoCrupier(){
        var puntos=_estadoUI.value.puntosCrupier
        val meta= _estadoUI.value.metaBlackjack
        if(puntos<(meta-4)){
            val carta=obtenerCarta()
            var lista=_estadoUI.value.cartasCrupier.toMutableList()
            var estados=_estadoUI.value.estadoCartasCrupier.toMutableList()
            var contador=_estadoUI.value.contadorCrupier
            var asc=_estadoUI.value.asCrupier
            var puntos= _estadoUI.value.puntosCrupier

            estados.add(contador==0)
            contador++
            lista.add(carta.identificador)
            lista.toList()
            estados.toList()
            asc+=(if(carta.valor==1) 1 else 0)

            if(carta.valor==1){
                if( (meta-puntos)>11) puntos+=11
                else puntos+=1
            }else{
                puntos+=carta.valor
            }

            _estadoUI.update { estadoActual ->
                estadoActual.copy(
                    cartasCrupier = lista,
                    estadoCartasCrupier = estados,
                    contadorCrupier = contador,
                    asCrupier = asc,
                    puntosCrupier = puntos
                )
            }
        }else{
            //Se planta el crupier
            _estadoUI.update { est -> est.copy(crupierPlantado = true) }
        }
    }

    fun botonTomar(){
        turnoJugador()
        turnoCrupier()
    }
    fun botonPlantar(){
        _estadoUI.update { est -> est.copy(jugadorPlantado = true) }
        var pC=_estadoUI.value.crupierPlantado
        //el crupier tomara cartas hasta plantarse
        do {
            turnoCrupier()
            pC=_estadoUI.value.crupierPlantado
        }while (pC!=true)
        voltearJugador()
        voltearCrupier()
        evaluarPuntos()
    }

    fun evaluarPuntos(){
        val meta=_estadoUI.value.metaBlackjack
        val jugador=estadoUI.value.puntosJugador
        val crupier=_estadoUI.value.puntosCrupier
        val tamJ=_estadoUI.value.contadorJugador
        val tamC=_estadoUI.value.contadorCrupier

        if(jugador>meta && crupier>meta){
            //ambos perdieron empate
            _estadoUI.update { est -> est.copy(resultado = 4) }
            return
        }

        if(jugador==meta&&crupier==meta){
            if (tamJ>tamC){
                //gana jugador
                _estadoUI.update { est -> est.copy(resultado = 1) }
            }else if (tamC>tamJ){
                //gana crupier
                _estadoUI.update { est -> est.copy(resultado = 2) }
            }else{
                //empate ambos gana
                _estadoUI.update { est -> est.copy(resultado = 3) }
            }
            return
        }

        if(jugador==meta&& crupier!=meta){
            //gano jugador
            _estadoUI.update { est -> est.copy(resultado = 1) }
            return
        }

        if(crupier==meta&& jugador!=meta){
            //gano crupier
            _estadoUI.update { est -> est.copy(resultado = 2) }
            return
        }
        if(jugador<=meta&& crupier>meta){
            //gano jugador
            _estadoUI.update { est -> est.copy(resultado = 1) }
            return
        }

        if(crupier<=meta&& jugador>meta){
            //gano crupier
            _estadoUI.update { est -> est.copy(resultado = 2) }
            return
        }

        val difC=meta-crupier
        val difJ=meta-jugador

        if(difC<difJ){
            //gana crupier
            _estadoUI.update { est -> est.copy(resultado = 2) }
        }else if(difJ<difC){
            //ganaJugador
            _estadoUI.update { est -> est.copy(resultado = 1) }
        }else{
            //empate
            _estadoUI.update { est -> est.copy(resultado =3) }
        }
    }

    fun voltearJugador(){
        var est=_estadoUI.value.estadoCartasJugador.toMutableList()
        for (i in 0.._estadoUI.value.contadorJugador-1) est.set(i, true)
        est.toList()
        _estadoUI.update {estadoActual -> estadoActual.copy(estadoCartasJugador=est)}
    }

    fun voltearCrupier(){
        var est=_estadoUI.value.estadoCartasCrupier.toMutableList()
        for (i in 0.._estadoUI.value.contadorCrupier-1) est.set(i, true)
        est.toList()
        _estadoUI.update {estadoActual -> estadoActual.copy(estadoCartasCrupier =est)}
    }

    fun voltearCartaJugador(index: Int,estado:Boolean){
        var esc= _estadoUI.value.estadoCartasJugador.toMutableList()
        esc.set(index,estado)
        esc.toList()
        _estadoUI.update{ estadoActual -> estadoActual.copy(estadoCartasJugador = esc)}
    }

    fun voltearCartaCrupier(index: Int,estado:Boolean){
        var esc= _estadoUI.value.estadoCartasCrupier.toMutableList()
        esc.set(index,estado)
        esc.toList()
        _estadoUI.update{ estadoActual -> estadoActual.copy(estadoCartasCrupier = esc)}
    }

    fun obtenerCarta():Carta{
        val rnd= (0..TAMANOMAZO-1).random()
        val cartaActual=mazo.elementAt(rnd)
        if(cartasUsadas.contains(cartaActual)) return obtenerCarta()
        else{
            cartasUsadas.add(cartaActual)
            return cartaActual
        }
    }
}

data class Carta(val identificador: String,val valor: Int)