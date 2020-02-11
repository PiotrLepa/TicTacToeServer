package com.piotr.tictactoe.user.domain.model

import com.piotr.tictactoe.user.dto.UserDto
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
  var email: String,

  var username: String,

  var password: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
) {

  constructor() : this("", "", "")

  fun toDto() = UserDto(
      id = id!!,
      email = email,
      username = username
  )
}