package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.ResetBoardDto

data class ResetBoard(
  val gameId: Long
) {

  fun toDto() = ResetBoardDto(
      gameId = gameId
  )

  companion object {
    fun fromDto(dto: ResetBoardDto) = ResetBoard(
        gameId = dto.gameId
    )
  }
}