package com.piotr.tictactoe.gameMove.domain

class InvalidFieldIndexRangeException(override val message: String? = null) : Exception(message)

class FieldAlreadyTakenException(override val message: String? = null) : Exception(message)