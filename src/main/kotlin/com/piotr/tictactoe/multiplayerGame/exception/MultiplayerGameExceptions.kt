package com.piotr.tictactoe.multiplayerGame.exception

class InvalidPlayerException(override val message: String? = null) : Exception(message)

class InvalidOpponentCodeException(override val message: String? = null) : Exception(message)

class OpponentMoveException(override val message: String? = null) : Exception(message)

class GameAlreadyStaredException(override val message: String? = null) : Exception(message)