package com.piotr.tictactoe.user.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Register(
  var email: String,
  var login: String,
  var password: String,
  var repeatedPassword: String,
  @Id @GeneratedValue var id: Long? = null
) {
  constructor() : this("", "", "", "")
}