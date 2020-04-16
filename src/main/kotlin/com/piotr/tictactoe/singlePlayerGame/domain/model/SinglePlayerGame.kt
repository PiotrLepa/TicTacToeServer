package com.piotr.tictactoe.singlePlayerGame.domain.model

import com.piotr.tictactoe.common.game.model.FieldMark
import com.piotr.tictactoe.common.game.model.GameStatus
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
@Table(name = "single_player_games")
data class SinglePlayerGame(

  @Column(name = "player_id")
  var playerId: Long,

  @Enumerated(EnumType.STRING)
  var status: GameStatus,

  @Column(name = "difficulty_level")
  @Enumerated(EnumType.STRING)
  var difficultyLevel: DifficultyLevel,

  @Column(name = "player_mark")
  @Enumerated(EnumType.STRING)
  var playerMark: FieldMark,

  @Column(name = "computer_mark")
  @Enumerated(EnumType.STRING)
  var computerMark: FieldMark,

  @Column(name = "creation_date")
  var creationDate: Long,

  @Column(name = "modification_date")
  var modificationDate: Long,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var gameId: Long? = null
) {

  constructor() : this(-1, GameStatus.ON_GOING, DifficultyLevel.EASY,
      FieldMark.X, FieldMark.O, DateTime.now().millis, DateTime.now().millis
  )
}