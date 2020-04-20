package com.piotr.tictactoe.singlePlayerGame.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.gameMove.domain.GameMoveFacade
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.gameResult.dto.SinglePlayerGameResultDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameStatus
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameTurn
import com.piotr.tictactoe.singlePlayerGame.domain.utils.SinglePlayerGameChecker
import com.piotr.tictactoe.singlePlayerGame.domain.utils.SinglePlayerGameHelper
import com.piotr.tictactoe.singlePlayerGame.domain.utils.computerMoveLogic.ComputerMoveLogic
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDto
import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SinglePlayerGameFacade @Autowired constructor(
  private val singlePlayerGameRepository: SinglePlayerGameRepository,
  private val userFacade: UserFacade,
  private val gameMoveFacade: GameMoveFacade,
  private val singlePlayerGameHelper: SinglePlayerGameHelper,
  private val singlePlayerGameChecker: SinglePlayerGameChecker,
  private val computerMoveLogic: ComputerMoveLogic,
  private val singlePlayerDtoMapperGame: Converter1<SinglePlayerGame, SinglePlayerGameDto, AllGameMovesDto>,
  private val singlePlayerDetailsDtoMapperSinglePlayer: Converter<SinglePlayerGame, SinglePlayerGameResultDto>
) {

  fun createSinglePlayerGame(difficultyLevel: DifficultyLevel): SinglePlayerGameDto {
    val player = userFacade.getLoggedUser()
    val startingPlayer = singlePlayerGameHelper.getStartingPlayer()
    val game = singlePlayerGameHelper.createSinglePlayerGame(player.id, difficultyLevel, startingPlayer)
        .let(singlePlayerGameRepository::save)
    val updatedMoves = if (startingPlayer == SinglePlayerGameTurn.COMPUTER) {
      doComputerMove(game, listOf())
    } else {
      listOf()
    }

    return updateGame(game, updatedMoves, game.playerMark)
  }

  fun setPlayerMoveAndGetComputerMove(gameId: Long, fieldIndex: Int): SinglePlayerGameDto {
    val game = singlePlayerGameRepository.findGameByGameId(gameId)
    val player = userFacade.getLoggedUser()

    singlePlayerGameChecker.checkIfGameIsOnGoing(game)
    singlePlayerGameChecker.checkIfPlayerMatch(game, player)

    gameMoveFacade.setMove(gameId, fieldIndex, game.playerMark)
    val moves = gameMoveFacade.getAllMoves(gameId).moves
    val updatedMoves = doComputerMove(game, moves)

    return updateGame(game, updatedMoves, game.playerMark)
  }

  private fun doComputerMove(game: SinglePlayerGame, moves: List<GameMoveDto>): List<GameMoveDto> {
    return if (singlePlayerGameHelper.isGameOnGoing(moves, game.playerMark)) {
      val computerMove = setComputeMove(game.gameId!!, game.difficultyLevel, game.computerMark, moves)
      moves + listOf(computerMove)
    } else {
      moves
    }
  }

  private fun updateGame(
    game: SinglePlayerGame,
    moves: List<GameMoveDto>,
    playerMark: FieldMark
  ): SinglePlayerGameDto {
    val gameToSave = game.copy(
        status = singlePlayerGameHelper.getGameStatus(moves, playerMark)
    )
    val savedGame = singlePlayerGameRepository.save(gameToSave)
    return singlePlayerDtoMapperGame.convert(savedGame, AllGameMovesDto(moves = moves))
  }

  private fun setComputeMove(
    gameId: Long,
    difficultyLevel: DifficultyLevel,
    computerMark: FieldMark,
    moves: List<GameMoveDto>
  ): GameMoveDto {
    val computerMoveFieldIndex = computerMoveLogic.calculateComputerMove(difficultyLevel, computerMark, moves)
    return gameMoveFacade.setMove(gameId, computerMoveFieldIndex, computerMark)
  }

  fun getUserGames(pageable: Pageable, playerId: Long): Page<SinglePlayerGameResultDto> =
      singlePlayerGameRepository.findAllByStatusInAndPlayerIdOrderByModificationDateDesc(
          pageable,
          SinglePlayerGameStatus.getEndedGameStatus(),
          playerId
      ).map(singlePlayerDetailsDtoMapperSinglePlayer::convert)

  fun getAllGames(pageable: Pageable): Page<SinglePlayerGameResultDto> =
      singlePlayerGameRepository.findAllByStatusInOrderByModificationDateDesc(
          pageable,
          SinglePlayerGameStatus.getEndedGameStatus()
      ).map(singlePlayerDetailsDtoMapperSinglePlayer::convert)

  fun getGameDetails(gameId: Long): SinglePlayerGameResultDto =
      singlePlayerGameRepository.findGameByGameId(gameId).let(singlePlayerDetailsDtoMapperSinglePlayer::convert)
}