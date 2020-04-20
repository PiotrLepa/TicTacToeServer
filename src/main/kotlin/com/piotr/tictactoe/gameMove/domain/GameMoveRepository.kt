package com.piotr.tictactoe.gameMove.domain

import com.piotr.tictactoe.gameMove.domain.model.GameMove
import com.piotr.tictactoe.gameMove.domain.model.GameMoveType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameMoveRepository : JpaRepository<GameMove, Long> {

  fun findMovesByGameIdAndMoveType(gameId: Long, type: GameMoveType): List<GameMove>

  fun findFirstMoveByGameIdOrderByMoveIdDesc(gameId: Long): GameMove?
}