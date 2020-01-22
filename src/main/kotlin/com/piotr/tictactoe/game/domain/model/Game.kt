package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.GameDto
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Game(
  @Id
  val gameId: Long?,
  val difficultyLevel: DifficultyLevel,
  @ElementCollection(targetClass = Field::class)
  val board: List<Field>,
  val playerMark: Mark,
  val aiMark: Mark,
  var status: GameStatus,
  var playerWins: Int,
  var playerDefeats: Int,
  var draws: Int
) {

  fun toDto() = GameDto(
      gameId = gameId,
      difficultyLevel = difficultyLevel,
      board = board.map(Field::toDto),
      playerMark = playerMark,
      aiMark = aiMark,
      status = status,
      playerWins = playerWins,
      playerDefeats = playerDefeats,
      draws = draws
  )

  companion object {
    fun fromDto(dto: GameDto) = Game(
        gameId = dto.gameId,
        difficultyLevel = dto.difficultyLevel,
        board = dto.board.map(Field.Companion::fromDto),
        playerMark = dto.playerMark,
        aiMark = dto.aiMark,
        status = dto.status,
        playerWins = dto.playerWins,
        playerDefeats = dto.playerDefeats,
        draws = dto.draws
    )
  }
}