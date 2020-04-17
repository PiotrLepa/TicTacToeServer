package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.multiplayerGame.converter.MultiplayerGameDtoConverterParams
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameDispatcher
import com.piotr.tictactoe.multiplayerGame.domain.utils.MultiplayerGameHelper
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameCreatedDto
import com.piotr.tictactoe.multiplayerGame.dto.MultiplayerGameDto
import com.piotr.tictactoe.multiplayerGame.exception.InvalidOpponentCodeException
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MultiplayerGameFacade {

  @Autowired
  private lateinit var multiplayerGameRepository: MultiplayerGameRepository

  @Autowired
  private lateinit var multiplayerGameHelper: MultiplayerGameHelper

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var multiplayerGameDispatcher: MultiplayerGameDispatcher

  @Autowired
  private lateinit var multiplayerGameDtoConverter: Converter1<MultiplayerGame, MultiplayerGameDto, MultiplayerGameDtoConverterParams>

  @Autowired
  private lateinit var multiplayerGameCreatedDtoConverter: Converter<MultiplayerGame, MultiplayerGameCreatedDto>

  fun createMultiplayerGame(opponentCode: String): MultiplayerGameCreatedDto {
    val firstPlayer = userFacade.getLoggedUser()
    val secondPlayer = userFacade.getUserByPlayerCode(opponentCode) ?: throw InvalidOpponentCodeException()

    val game = multiplayerGameHelper.createMultiplayerGame(firstPlayer, secondPlayer)
        .let(multiplayerGameRepository::save)

    val converterParams = MultiplayerGameDtoConverterParams(
        firstPlayerCode = firstPlayer.playerCode,
        secondPlayerCode = secondPlayer.playerCode,
        moves = listOf()
    )
    multiplayerGameDispatcher.initGame(multiplayerGameDtoConverter.convert(game, converterParams))

    return multiplayerGameCreatedDtoConverter.convert(game)
  }

  fun setPlayerMove(gameId: Long, fieldIndex: Int) {
  }
}