package com.piotr.tictactoe.gameResult.domain

import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.MultiplayerGameResultDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDetailsDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDto
import com.piotr.tictactoe.gameResult.exception.GameIsOnGoingException
import com.piotr.tictactoe.multiplayerGame.domain.MultiplayerGameFacade
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import com.piotr.tictactoe.singlePlayerGame.domain.SinglePlayerGameFacade
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class GameResultFacade @Autowired constructor(
  private val userFacade: UserFacade,
  private val singlePlayerGameFacade: SinglePlayerGameFacade,
  private val multiplayerGameFacade: MultiplayerGameFacade,
  private val gameMoveFacade: GameMoveFacade,
  private val singlePlayerGameResultDetailsConverter: Converter1<SinglePlayerGameResultDto, SinglePlayerGameResultDetailsDto, AllGameMovesDto>,
  private val multiplayerGameResultDetailsConverter: Converter1<MultiplayerGameResultDto, MultiplayerGameResultDetailsDto, AllGameMovesDto>
) {

  fun getSinglePlayerUserResults(pageable: Pageable): Page<SinglePlayerGameResultDto> {
    val player = userFacade.getLoggedUser()
    return singlePlayerGameFacade.getUserGames(pageable, player.id)
  }

  fun getSinglePlayerAllResults(pageable: Pageable): Page<SinglePlayerGameResultDto> =
      singlePlayerGameFacade.getAllGames(pageable)

  fun getSinglePlayerResultDetails(gameId: Long): SinglePlayerGameResultDetailsDto {
    val game = singlePlayerGameFacade.getGameDetails(gameId)
    checkIfSinglePlayerGameDidEnd(game)
    val allMoves = gameMoveFacade.getSinglePlayerAllMoves(gameId)
    return singlePlayerGameResultDetailsConverter.convert(game, allMoves)
  }

  fun getMultiplayerUserResults(pageable: Pageable): Page<MultiplayerGameResultDto> {
    val player = userFacade.getLoggedUser()
    return multiplayerGameFacade.getUserGames(pageable, player.id)
  }

  fun getMultiplayerAllResults(pageable: Pageable): Page<MultiplayerGameResultDto> =
      multiplayerGameFacade.getAllGames(pageable)

  fun getMultiplayerResultDetails(gameId: Long): MultiplayerGameResultDetailsDto {
    val game = multiplayerGameFacade.getGameDetails(gameId)
    checkIfMultiplayerGameDidEnd(game)
    val allMoves = gameMoveFacade.getMultiplayerAllMoves(gameId)
    return multiplayerGameResultDetailsConverter.convert(game, allMoves)
  }

  private fun checkIfSinglePlayerGameDidEnd(game: SinglePlayerGameResultDto) {
    if (game.status !in SinglePlayerGameStatus.getEndedGameStatus()) {
      throw GameIsOnGoingException()
    }
  }

  private fun checkIfMultiplayerGameDidEnd(game: MultiplayerGameResultDto) {
    if (game.status !in MultiplayerGameStatus.getEndedGameStatus()) {
      throw GameIsOnGoingException()
    }
  }
}