package com.piotr.tictactoe.game.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "games_with_computer")
data class GameWithComputer(

  @Column(name = "session_id")
  var sessionId: Long,

  @Column(name = "first_player_id")
  var firstPlayerId: Long,

  @Enumerated(EnumType.STRING)
  var status: GameStatus,

  @Column(name = "difficulty_level")
  @Enumerated(EnumType.STRING)
  var difficultyLevel: DifficultyLevel,

  @Column(name = "creation_date")
  var creationDate: Long,

  @Column(name = "modification_date")
  var modificationDate: Long,

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null
)