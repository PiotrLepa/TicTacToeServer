package com.piotr.tictactoe.domain.component

import com.piotr.tictactoe.domain.dto.DifficultyLevel
import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.PlayerMoveDto

interface GameComponent {

  fun createGame(difficultyLevel: DifficultyLevel, gameId: Long = System.nanoTime()): GameDto

  fun setField(
    game: GameDto,
    playerMove: PlayerMoveDto
  ): GameDto
}