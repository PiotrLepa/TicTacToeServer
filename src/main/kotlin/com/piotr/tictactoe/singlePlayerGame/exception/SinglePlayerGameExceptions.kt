package com.piotr.tictactoe.singlePlayerGame.exception

class GameEndedException(override val message: String? = null) : Exception(message)

class WrongPlayerException(override val message: String? = null) : Exception(message)