package com.piotr.tictactoe.game.domain.model

import com.piotr.tictactoe.game.dto.PlayerMoveDto

data class PlayerMove(
  val gameId: Long,
  val field: Field
) {

  fun toDto() = PlayerMoveDto(
      gameId = gameId,
      field = field.toDto()
  )

  companion object {
    fun fromDto(dto: PlayerMoveDto) = PlayerMove(
        gameId = dto.gameId,
        field = Field.fromDto(dto.field)
    )
  }
}