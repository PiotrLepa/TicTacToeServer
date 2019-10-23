package com.piotr.tictactoe.service

import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.PlayerMoveDto
import com.piotr.tictactoe.domain.dto.ResetGameDto

interface GameService {

  fun createGame(): GameDto

  fun setField(playerMove: PlayerMoveDto): GameDto

  fun resetGame(resetGame: ResetGameDto): GameDto
}
