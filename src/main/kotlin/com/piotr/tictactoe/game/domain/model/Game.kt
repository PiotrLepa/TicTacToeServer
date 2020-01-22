package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.FieldDto
import com.piotr.tictactoe.game.dto.GameDto

data class Game(
  val gameId: Long,
  val difficultyLevel: DifficultyLevel,
  val board: List<Field>,
  val playerMark: Mark,
  val aiMark: Mark,
  var status: GameStatus,
  var playerWins: Int,
  var playerDefeats: Int,
  var draws: Int
) {

  companion object {
    fun fromDto(dto: GameDto) = Game(
        gameId = dto.gameId,
        difficultyLevel = dto.difficultyLevel,
        board = dto.board.map(::mapField),
        playerMark = dto.playerMark,
        aiMark = dto.aiMark,
        status = dto.status,
        playerWins = dto.playerWins,
        playerDefeats = dto.playerDefeats,
        draws = dto.draws
    )

    private fun mapField(dto: FieldDto) = Field(
        index = dto.index,
        mark = dto.mark
    )
  }
}