package com.piotr.tictactoe.user.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.UserDto
import org.springframework.stereotype.Component

@Component
class UserDtoConverter : Converter<User, UserDto> {

  override fun convert(from: User) = UserDto(
      id = from.id!!,
      email = from.email,
      username = from.username,
      playerCode = from.playerCode
  )
}