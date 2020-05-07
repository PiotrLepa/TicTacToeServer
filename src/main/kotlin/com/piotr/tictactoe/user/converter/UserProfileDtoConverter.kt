package com.piotr.tictactoe.user.converter

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.user.domain.model.User
import com.piotr.tictactoe.user.dto.UserProfileDto
import org.springframework.stereotype.Component

@Component
class UserProfileDtoConverter : Converter<User, UserProfileDto> {

  override fun convert(from: User) = UserProfileDto(
      email = from.email,
      username = from.username,
      playerCode = from.playerCode
  )
}