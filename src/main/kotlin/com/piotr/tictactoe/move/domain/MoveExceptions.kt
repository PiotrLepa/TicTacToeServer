package com.piotr.tictactoe.move.domain

class InvalidFieldIndexRangeException(override val message: String? = null) : Exception(message)

class FieldAlreadyTakenException(override val message: String? = null) : Exception(message)