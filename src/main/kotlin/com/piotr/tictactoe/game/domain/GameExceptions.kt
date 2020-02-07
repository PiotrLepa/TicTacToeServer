package com.piotr.tictactoe.game.domain

class GameEndedException(override val message: String? = null) : Exception(message)