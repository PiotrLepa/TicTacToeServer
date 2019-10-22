package com.piotr.tictactoe.service

import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.PlayerMoveDto

interface GameService {

  fun createGame(): GameDto

  fun setField(playerMove: PlayerMoveDto): GameDto
}
