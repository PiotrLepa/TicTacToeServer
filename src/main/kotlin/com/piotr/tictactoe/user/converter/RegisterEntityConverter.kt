package com.piotr.tictactoe.user.converter

import com.piotr.tictactoe.core.converter.ConverterWithArgs
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.RegisterDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class RegisterEntityConverter(
  private val passwordEncoder: PasswordEncoder
) : ConverterWithArgs<RegisterDto, User, RegisterEntityConverterArgs> {

  override fun convert(from: RegisterDto, args: RegisterEntityConverterArgs) = User(
      email = from.email,
      username = from.username,
      password = passwordEncoder.encode(from.password),
      languageTag = args.languageTag,
      deviceToken = args.deviceToken,
      playerCode = args.playerCode
  )
}