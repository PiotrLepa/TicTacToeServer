package com.piotr.tictactoe.game.dto

import com.piotr.tictactoe.game.domain.model.Mark

data class FieldDto(
  val index: Int,
  var mark: Mark
)