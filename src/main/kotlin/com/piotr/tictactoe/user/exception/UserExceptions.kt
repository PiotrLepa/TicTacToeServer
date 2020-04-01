package com.piotr.tictactoe.user.exception

class EmailAlreadyExistsException(override val message: String? = null) : Exception(message)

class UsernameAlreadyExistsException(override val message: String? = null) : Exception(message)

class UsernameTooShortException(override val message: String? = null) : Exception(message)

class PasswordTooShortException(override val message: String? = null) : Exception(message)

class PasswordsAreDifferentException(override val message: String? = null) : Exception(message)