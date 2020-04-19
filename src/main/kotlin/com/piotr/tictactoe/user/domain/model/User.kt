package com.piotr.tictactoe.user.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
  var email: String,

  var username: String,

  var password: String,

  @Column(name = "game_code")
  var playerCode: String,

  @Column(name = "device_token")
  var deviceToken: String,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null
) {

  constructor() : this("", "", "", "", "")
}