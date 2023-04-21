package com.argent.blackjack

data class GameUiState(
    val puntosCrupier: Int =0,
    val puntosJugador: Int = 0,
    val jugadorPlantado: Boolean= false,
    val crupierPlantado: Boolean= false,
    val juegoTerminado: Boolean= false,
    val cartasCrupier: List<String> = listOf<String>(),
    val cartasJugador: List<String> = listOf<String>(),
    var estadoCartasCrupier : List<Boolean> = listOf<Boolean>(),
    var estadoCartasJugador : List<Boolean> = listOf<Boolean>(),
    val contadorJugador:Int=0,
    val contadorCrupier:Int=0,
    val asJugador:Int=0,
    val asCrupier:Int=0,
    val metaBlackjack:Int=21,
    val resultado:Int=0     //1- Jugador   2-Crupier    3-Empate Win    4-Empate Lose
)