package com.piotr.tictactoe.singlePlayerGame.exception

class GameFinishedException(override val message: String? = null) : Exception(message)

class GameNotFinishedException(override val message: String? = null) : Exception(message)

class WrongPlayerException(override val message: String? = null) : Exception(message)