package com.piotr.tictactoe.multiplayerGame.domain.model

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
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
  val firstPlayerId: Long,

  @Column(name = "second_player_id")
  val secondPlayerId: Long,

  @Enumerated(EnumType.STRING)
  val status: MultiplayerGameStatus,

  @Enumerated(EnumType.STRING)
  val currentTurn: MultiplayerGameTurn,

  @Column(name = "first_player_mark")
  @Enumerated(EnumType.STRING)
  val firstPlayerMark: FieldMark,

  @Column(name = "second_player_mark")
  @Enumerated(EnumType.STRING)
  val secondPlayerMark: FieldMark,

  @Column(name = "creation_date")
  @CreationTimestamp
  val creationDate: Timestamp = Timestamp(0),

  @Column(name = "modification_date")
  @UpdateTimestamp
  val modificationDate: Timestamp = Timestamp(0),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val gameId: Long? = null
) {

  constructor() : this(-1, -1, MultiplayerGameStatus.ON_GOING,
      MultiplayerGameTurn.FIRST_PLAYER, FieldMark.X, FieldMark.O
  )
}