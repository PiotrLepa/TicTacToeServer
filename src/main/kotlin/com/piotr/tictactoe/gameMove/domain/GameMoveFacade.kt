package com.piotr.tictactoe.gameMove.domain

import com.piotr.tictactoe.core.converter.Converter
import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import com.piotr.tictactoe.gameMove.domain.model.GameMove
import com.piotr.tictactoe.gameMove.dto.AllGameMovesDto
import com.piotr.tictactoe.gameMove.dto.GameMoveDto
import com.piotr.tictactoe.utils.GameConstant.VALID_FIELDS_INDEXES
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class GameMoveFacade @Autowired constructor(
  private val gameMoveRepository: GameMoveRepository,
  private val gameMoveDtoConverter: Converter<GameMove, GameMoveDto>
) {

  fun setMove(gameId: Long, fieldIndex: Int, mark: FieldMark): GameMoveDto {
    checkFieldIndexRange(fieldIndex)
    checkIfFieldIndexIsEmpty(gameId, fieldIndex)
    return GameMove(
        gameId = gameId,
        fieldIndex = fieldIndex,
        counter = getNextCounter(gameId),
        mark = mark,
        creationDate = DateTime.now().millis
    ).let(gameMoveRepository::save)
        .let(gameMoveDtoConverter::convert)
  }

  fun getAllMoves(gameId: Long): AllGameMovesDto {
    val movies = gameMoveRepository.findMovesByGameId(gameId).map(gameMoveDtoConverter::convert)
    return AllGameMovesDto(moves = movies)
  }

  private fun getNextCounter(gameId: Long): Int {
    val lastMove = gameMoveRepository.findFirstMoveByGameIdOrderByMoveIdDesc(gameId)
    return lastMove?.counter?.plus(1) ?: 0
  }

  private fun checkFieldIndexRange(fieldIndex: Int) {
    if (fieldIndex !in VALID_FIELDS_INDEXES) {
      throw InvalidFieldIndexRangeException("Field index should be in range $VALID_FIELDS_INDEXES")
    }
  }

  private fun checkIfFieldIndexIsEmpty(gameId: Long, fieldIndex: Int) {
    val allMovies = getAllMoves(gameId)
    if (allMovies.moves.any { it.fieldIndex == fieldIndex }) {
      throw FieldAlreadyTakenException()
    }
  }
}