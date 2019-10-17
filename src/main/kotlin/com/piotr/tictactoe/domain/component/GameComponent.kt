package com.piotr.tictactoe.domain.component

import com.piotr.tictactoe.domain.dto.DifficultyLevel
import com.piotr.tictactoe.domain.dto.GameDto

interface GameComponent {

  fun createGameBoard(difficultyLevel: DifficultyLevel): GameDto
}