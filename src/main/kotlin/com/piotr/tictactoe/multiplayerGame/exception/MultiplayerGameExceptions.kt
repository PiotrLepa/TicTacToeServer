package com.piotr.tictactoe.multiplayerGame.exception

class InvalidOpponentCodeException(override val message: String? = null) : Exception(message)

class OpponentMoveException(override val message: String? = null) : Exception(message)