package com.piotr.tictactoe.game.domain.model

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

data class Player(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  val id: Long?,

  val username: String,

  val email: String,

  val password: String,

  val created: Long
)