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
@Table(name = "games_with_players")
data class GameWithPlayer(

  @Column(name = "session_id")
  var sessionId: Long,

  @Column(name = "first_player_id")
  var firstPlayerId: Long,

  @Column(name = "second_player_id")
  var secondPlayerId: Long,

  @Enumerated(EnumType.STRING)
  var status: GameStatus,

  @Column(name = "creation_date")
  var creationDate: Long,

  @Column(name = "modification_date")
  var modificationDate: Long,

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null
)