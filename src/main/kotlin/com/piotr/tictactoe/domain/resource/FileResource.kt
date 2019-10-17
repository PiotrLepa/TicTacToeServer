package com.piotr.tictactoe.domain.resource

import com.piotr.tictactoe.domain.dto.GameDto

interface FileResource {

  fun saveGame(game: GameDto)
}