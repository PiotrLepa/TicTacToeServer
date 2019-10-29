package com.piotr.tictactoe.service

import com.piotr.tictactoe.domain.dto.GameDto
import com.piotr.tictactoe.domain.dto.PlayerMoveDto
import com.piotr.tictactoe.domain.dto.ResetBoardDto

interface GameService {

  fun createGame(): GameDto

  fun setField(playerMove: PlayerMoveDto): GameDto

  fun resetBoard(resetBoard: ResetBoardDto): GameDto
}
