package com.piotr.tictactoe.domain.component

import com.piotr.tictactoe.domain.dto.GameDto

interface AiMoveComponent {

  fun setFieldByAi(gameDto: GameDto): GameDto
}