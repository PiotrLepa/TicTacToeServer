package com.piotr.tictactoe.game.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Move(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  val id: Long?,

  @ManyToOne
  @JoinColumn(name = "game_id", nullable = false)
  val game: Game,

  val fieldIndex: Int,

  val selectedFieldCount: Int,

  val created: Long
)