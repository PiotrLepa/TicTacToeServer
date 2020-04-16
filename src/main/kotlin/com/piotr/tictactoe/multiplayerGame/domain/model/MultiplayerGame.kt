package com.piotr.tictactoe.multiplayerGame.domain.model

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import org.joda.time.DateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "multiplayer_games")
data class MultiplayerGame(

  @Column(name = "first_player_id")
  var firstPlayerId: Long,

  @Column(name = "second_player_id")
  var secondPlayerId: Long,

  @Enumerated(EnumType.STRING)
  var status: MultiplayerGameStatus,

  @Enumerated(EnumType.STRING)
  var currentTurn: MultiplayerGameTurn,

  @Column(name = "first_player_mark")
  @Enumerated(EnumType.STRING)
  var firstPlayerMark: FieldMark,

  @Column(name = "second_player_mark")
  @Enumerated(EnumType.STRING)
  var secondPlayerMark: FieldMark,

  @Column(name = "creation_date")
  var creationDate: Long,

  @Column(name = "modification_date")
  var modificationDate: Long,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var gameId: Long? = null
) {

  constructor() : this(-1, -1, MultiplayerGameStatus.ON_GOING,
      MultiplayerGameTurn.FIRST_PLAYER, FieldMark.X, FieldMark.O,
      DateTime.now().millis, DateTime.now().millis
  )
}