package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface MultiplayerGameRepository : JpaRepository<MultiplayerGame, Long> {

  fun findGameByGameId(id: Long): MultiplayerGame

  @Query("""SELECT g
    FROM MultiplayerGame g
    WHERE g.firstPlayerId = :playerId OR g.secondPlayerId = :playerId
    ORDER BY g.gameId DESC""")
  fun findRecentGameByPlayerId(playerId: Long, pageable: Pageable): Page<MultiplayerGame>

  fun findAllByStatusInOrderByModificationDateDesc(pageable: Pageable, status: List<MultiplayerGameStatus>): Page<MultiplayerGame>

  @Query("""SELECT game FROM MultiplayerGame game
      WHERE game.status IN :status AND (game.firstPlayerId = :playerId OR game.secondPlayerId = :playerId) 
      ORDER BY game.modificationDate DESC""")
  fun findPlayerGameResultsOrderByModificationDateDesc(
    pageable: Pageable,
    status: List<MultiplayerGameStatus>,
    playerId: Long
  ): Page<MultiplayerGame>
}