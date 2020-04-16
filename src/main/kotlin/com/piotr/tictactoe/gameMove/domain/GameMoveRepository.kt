package com.piotr.tictactoe.gameMove.domain

import com.piotr.tictactoe.gameMove.domain.model.GameMove
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameMoveRepository : JpaRepository<GameMove, Long> {

  fun findMovesByGameId(gameId: Long): List<GameMove>

  fun findFirstMoveByGameIdOrderByMoveIdDesc(gameId: Long): GameMove?
}