package com.piotr.tictactoe.multiplayerGame.domain

import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGame
import com.piotr.tictactoe.multiplayerGame.domain.model.MultiplayerGameStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MultiplayerGameRepository : JpaRepository<MultiplayerGame, Long> {

  fun findGameByGameId(id: Long): MultiplayerGame

  fun findAllByStatusInOrderByModificationDateDesc(pageable: Pageable, status: List<MultiplayerGameStatus>): Page<MultiplayerGame>
}