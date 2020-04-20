package com.piotr.tictactoe.gameMove.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.domain.model.GameMove
import com.piotr.tictactoe.gameMove.domain.model.GameMoveType
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.utils.GameConstant.VALID_FIELDS_INDEXES
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameMoveFacade @Autowired constructor(
  private val gameMoveRepository: GameMoveRepository,
  private val gameMoveDtoConverter: Converter<GameMove, GameMoveDto>
) {

  fun setSinglePlayerMove(gameId: Long, fieldIndex: Int, mark: FieldMark): AllGameMovesDto =
      setMove(gameId, fieldIndex, mark, GameMoveType.SINGLE_PLAYER)

  fun setMultiplayerMove(gameId: Long, fieldIndex: Int, mark: FieldMark): AllGameMovesDto =
      setMove(gameId, fieldIndex, mark, GameMoveType.MULTIPLAYER)

  private fun setMove(gameId: Long, fieldIndex: Int, mark: FieldMark, type: GameMoveType): AllGameMovesDto {
    val moves = getAllMoves(gameId, type).moves

    checkFieldIndexRange(fieldIndex)
    checkIfFieldIndexIsEmpty(moves, fieldIndex)

    val newMove = GameMove(
        gameId = gameId,
        fieldIndex = fieldIndex,
        counter = getNextCounter(gameId),
        mark = mark,
        moveType = type
    ).let(gameMoveRepository::save)
        .let(gameMoveDtoConverter::convert)

    return AllGameMovesDto(moves + newMove)
  }

  private fun getNextCounter(gameId: Long): Int {
    val lastMove = gameMoveRepository.findFirstMoveByGameIdOrderByMoveIdDesc(gameId)
    return lastMove?.counter?.plus(1) ?: 0
  }

  fun getSinglePlayerAllMoves(gameId: Long): AllGameMovesDto =
      getAllMoves(gameId, GameMoveType.SINGLE_PLAYER)

  fun getMultiplayerAllMoves(gameId: Long): AllGameMovesDto =
      getAllMoves(gameId, GameMoveType.MULTIPLAYER)

  private fun getAllMoves(gameId: Long, type: GameMoveType): AllGameMovesDto {
    val movies = gameMoveRepository.findMovesByGameIdAndMoveType(gameId, type).map(gameMoveDtoConverter::convert)
    return AllGameMovesDto(moves = movies)
  }

  private fun checkFieldIndexRange(fieldIndex: Int) {
    if (fieldIndex !in VALID_FIELDS_INDEXES) {
      throw InvalidFieldIndexRangeException("Field index should be in range $VALID_FIELDS_INDEXES")
    }
  }

  private fun checkIfFieldIndexIsEmpty(
    moves: List<GameMoveDto>,
    fieldIndex: Int
  ) {
    if (moves.any { it.fieldIndex == fieldIndex }) {
      throw FieldAlreadyTakenException()
    }
  }
}