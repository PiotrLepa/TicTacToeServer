package com.piotr.tictactoe.user.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class User(
  var email: String,
  var username: String,
  var password: String,
  @Id @GeneratedValue var id: Long? = null
) {

  constructor() : this("", "", "")
}