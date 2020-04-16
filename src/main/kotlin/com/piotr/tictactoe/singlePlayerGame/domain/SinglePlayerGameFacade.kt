package com.piotr.tictactoe.singlePlayerGame.domain

import com.piotr.tictactoe.common.game.model.GameStatus
import com.piotr.tictactoe.common.game.model.GameStatus.ON_GOING
import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.core.converter.Converter1
import com.piotr.tictactoe.move.domain.MoveFacade
import com.piotr.tictactoe.move.domain.model.FieldMark
import com.piotr.tictactoe.move.dto.AllMovesDto
import com.piotr.tictactoe.move.dto.MoveDto
import com.piotr.tictactoe.singlePlayerGame.domain.model.DifficultyLevel
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGame
import com.piotr.tictactoe.singlePlayerGame.domain.model.SinglePlayerGameTurn
import com.piotr.tictactoe.singlePlayerGame.domain.utils.SinglePlayerGameHelper
import com.piotr.tictactoe.singlePlayerGame.domain.utils.computerMoveLogic.ComputerMoveLogic
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDetailsDto
import com.piotr.tictactoe.singlePlayerGame.dto.SinglePlayerGameDto
import com.piotr.tictactoe.singlePlayerGame.exception.GameEndedException
import com.piotr.tictactoe.singlePlayerGame.exception.WrongPlayerException
import com.piotr.tictactoe.user.domain.UserFacade
import com.piotr.tictactoe.utils.GameEndChecker
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SinglePlayerGameFacade {

  @Autowired
  private lateinit var singlePlayerGameRepository: SinglePlayerGameRepository

  @Autowired
  private lateinit var userFacade: UserFacade

  @Autowired
  private lateinit var moveFacade: MoveFacade

  @Autowired
  private lateinit var singlePlayerGameHelper: SinglePlayerGameHelper

  @Autowired
  private lateinit var computerMoveLogic: ComputerMoveLogic

  @Autowired
  private lateinit var gameEndChecker: GameEndChecker

  @Autowired
  private lateinit var singlePlayerDtoMapper: Converter1<SinglePlayerGame, SinglePlayerGameDto, AllMovesDto>

  @Autowired
  private lateinit var singlePlayerDetailsDtoMapper: Converter<SinglePlayerGame, SinglePlayerGameDetailsDto>

  fun createSinglePlayerGame(difficultyLevel: DifficultyLevel): SinglePlayerGameDto {
    val player = userFacade.getLoggedUser()
    val startingPlayer = singlePlayerGameHelper.getStartingPlayer()
    val game = singlePlayerGameHelper.createSinglePlayerGame(player.id, difficultyLevel, startingPlayer)
        .let(singlePlayerGameRepository::save)
    val moves = if (startingPlayer == SinglePlayerGameTurn.COMPUTER) {
      val computerMove = setComputeMove(game.gameId!!, game.difficultyLevel, game.computerMark, listOf())
      listOf(computerMove)
    } else {
      listOf()
    }
    return updateGame(game, moves, game.playerMark, game.computerMark)
  }

  fun setPlayerMoveAndGetComputerMove(gameId: Long, fieldIndex: Int): SinglePlayerGameDto {
    val game = singlePlayerGameRepository.findGameByGameId(gameId)
    checkIfGameIsOnGoing(game)
    checkIfPlayerMatch(game)
    moveFacade.setMove(gameId, fieldIndex, game.playerMark)
    val allMovesDto = moveFacade.getAllMoves(gameId)
    val moves = allMovesDto.moves
    val updatedMoves = if (canDoNextMove(moves, game.playerMark, game.computerMark)) {
      val computerMove = setComputeMove(gameId, game.difficultyLevel, game.computerMark, moves)
      moves + listOf(computerMove)
    } else {
      moves
    }
    return updateGame(game, updatedMoves, game.playerMark, game.computerMark)
  }

  fun getUserGames(pageable: Pageable, playerId: Long): Page<SinglePlayerGameDetailsDto> =
      singlePlayerGameRepository.findAllByStatusInAndPlayerIdOrderByModificationDateDesc(
          pageable,
          GameStatus.getEndedGameStatus(),
          playerId
      ).map(singlePlayerDetailsDtoMapper::convert)

  fun getAllGames(pageable: Pageable): Page<SinglePlayerGameDetailsDto> =
      singlePlayerGameRepository.findAllByStatusInOrderByModificationDateDesc(
          pageable,
          GameStatus.getEndedGameStatus()
      ).map(singlePlayerDetailsDtoMapper::convert)

  fun getGameDetails(gameId: Long): SinglePlayerGameDetailsDto =
      singlePlayerGameRepository.findGameByGameId(gameId).let(singlePlayerDetailsDtoMapper::convert)

  private fun updateGame(
    game: SinglePlayerGame,
    moves: List<MoveDto>,
    playerMark: FieldMark,
    computerMark: FieldMark
  ): SinglePlayerGameDto {
    val gameToSave = game.apply {
      status = gameEndChecker.checkGameEnd(moves, playerMark, computerMark)
      modificationDate = DateTime.now().millis
    }
    val savedGame = singlePlayerGameRepository.save(gameToSave)
    return singlePlayerDtoMapper.convert(savedGame, AllMovesDto(moves = moves))
  }

  private fun setComputeMove(
    gameId: Long,
    difficultyLevel: DifficultyLevel,
    computerMark: FieldMark,
    moves: List<MoveDto>
  ): MoveDto {
    val computerMoveFieldIndex = computerMoveLogic.calculateComputerMove(difficultyLevel, computerMark, moves)
    return moveFacade.setMove(gameId, computerMoveFieldIndex, computerMark)
  }

  private fun canDoNextMove(moves: List<MoveDto>, playerMark: FieldMark, computerMark: FieldMark) =
      gameEndChecker.checkGameEnd(moves, playerMark, computerMark) == ON_GOING

  private fun checkIfPlayerMatch(game: SinglePlayerGame) {
    val player = userFacade.getLoggedUser()
    if (player.id != game.playerId) {
      throw WrongPlayerException()
    }
  }

  private fun checkIfGameIsOnGoing(game: SinglePlayerGame) {
    if (game.status != ON_GOING) {
      throw GameEndedException()
    }
  }
}