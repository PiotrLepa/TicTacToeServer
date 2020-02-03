package com.piotr.tictactoe.game.domain

class GameEndedException : Exception()

class GameAlreadyEndedPlayerWon : Exception()
class GameAlreadyEndedComputerWon : Exception()
class GameAlreadyEndedDraw : Exception()