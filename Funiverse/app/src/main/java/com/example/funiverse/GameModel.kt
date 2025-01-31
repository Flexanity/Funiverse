package com.example.funiverse

import kotlin.random.Random

data class GameModel (
    var gameId : String = "-1",
    var winner : String ="",
    var gameStatus : GameStatus = GameStatus.CREATED,
    var currentPlayer : String = (arrayOf("1","2"))[Random.nextInt(2)],
    var currentLetter : String = "A",
    var theme : String = "Bollywood"
)


enum class GameStatus{
    CREATED,
    JOINED,
    INPROGRESS,
    FINISHED
}