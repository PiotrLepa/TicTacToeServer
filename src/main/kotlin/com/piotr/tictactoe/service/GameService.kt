package com.piotr.tictactoe.service

import com.piotr.tictactoe.domain.dto.GameDto

interface GameService {

  fun createGame(): GameDto
}
