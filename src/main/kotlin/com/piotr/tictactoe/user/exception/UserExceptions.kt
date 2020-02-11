package com.piotr.tictactoe.user.exception

class EmailAlreadyExistsException(override val message: String? = null) : Exception(message)

class PasswordsAreDifferentException(override val message: String? = null) : Exception(message)

class UserNotExistsException(override val message: String? = null) : Exception(message)