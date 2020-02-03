package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.model.FieldMark
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
@Table(name = "games_with_computer")
data class GameWithComputer(

  @Column(name = "player_id")
  var playerId: Long,

  @Enumerated(EnumType.STRING)
  var status: GameStatus,

  @Column(name = "difficulty_level")
  @Enumerated(EnumType.STRING)
  var difficultyLevel: DifficultyLevel,

  @Column(name = "current_turn")
  var currentTurn: GameTurn,

  @Column(name = "player_mark")
  var playerMark: FieldMark,

  @Column(name = "computer_mark")
  var computerMark: FieldMark,

  @Column(name = "creation_date")
  var creationDate: Long,

  @Column(name = "modification_date")
  var modificationDate: Long,

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null
) {

  constructor() : this(-1, GameStatus.IN_PROGRESS, DifficultyLevel.EASY,
      GameTurn.COMPUTER, FieldMark.X, FieldMark.O,
      DateTime.now().millis, DateTime.now().millis
  )

  fun toDto() = GameWithComputerDto(
      id = id!!,
      playerId = playerId,
      status = status,
      difficultyLevel = difficultyLevel,
      currentTurn = currentTurn
  )
}