package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.GameResultDto
import com.piotr.tictactoe.game.dto.GameWithComputerDto
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.MoveDto
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

  fun toDto(moves: List<MoveDto>) = GameWithComputerDto(
      gameId = gameId!!,
      playerId = playerId,
      status = status,
      difficultyLevel = difficultyLevel,
      playerMark = playerMark,
      computerMark = computerMark,
      moves = moves
  )

  fun toResultDto() = GameResultDto(
      gameId = gameId!!,
      playerId = playerId,
      status = status,
      difficultyLevel = difficultyLevel,
      playerMark = playerMark,
      computerMark = computerMark,
      startDate = creationDate,
      endDate = modificationDate
  )
}