package com.piotr.tictactoe.gameResult.domain

import com.piotr.tictactoe.common.game.model.GameStatus
import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.GameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.GameResultDto
import com.piotr.tictactoe.gameResult.exception.GameIsOnGoingException
import com.piotr.tictactoe.singlePlayerGame.domain.SinglePlayerGameFacade
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GameResultFacade {

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var singlePlayerGameFacade: SinglePlayerGameFacade

  @Autowired
  private lateinit var gameMoveFacade: GameMoveFacade

  @Autowired
  private lateinit var gameResultConverter: Converter<SinglePlayerGameDetailsDto, GameResultDto>

  @Autowired
  private lateinit var gameResultDetailsConverter: Converter1<SinglePlayerGameDetailsDto, GameResultDetailsDto, AllGameMovesDto>

  fun getUserGameResults(pageable: Pageable): Page<GameResultDto> {
    val player = userFacade.getLoggedUser()
    return singlePlayerGameFacade.getUserGames(pageable, player.id)
        .map(gameResultConverter::convert)
  }

  fun getAllGameResults(pageable: Pageable): Page<GameResultDto> =
      singlePlayerGameFacade.getAllGames(pageable)
          .map(gameResultConverter::convert)

  fun getGameResultDetails(gameId: Long): GameResultDetailsDto {
    val game = singlePlayerGameFacade.getGameDetails(gameId)
    checkIfGameDidEnd(game)
    val allMoves = gameMoveFacade.getAllMoves(gameId)
    return gameResultDetailsConverter.convert(game, allMoves)
  }

  private fun checkIfGameDidEnd(game: SinglePlayerGameDetailsDto) {
    if (game.status !in GameStatus.getEndedGameStatus()) {
      throw GameIsOnGoingException()
    }
  }
}