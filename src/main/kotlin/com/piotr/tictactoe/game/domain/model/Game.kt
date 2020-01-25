package com.piotr.tictactoe.game.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Game(
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(nullable = false)
  val id: Long?,

  val sessionId: Long,

//  @ManyToOne
//  @JoinColumn(name = "first_player_id", nullable = false)
//  val firstPlayer: Player,
//
//  @ManyToOne
//  @JoinColumn(name = "second_player_id", nullable = true)
//  val secondPlayer: Player?,

  @Enumerated(EnumType.STRING)
  val difficultyLevel: DifficultyLevel,

  @Enumerated(EnumType.STRING)
  var status: GameStatus,

  val created: Long
//  val currentTurn
) {

//  fun toDto() = GameDto(
//      gameId = gameId,
//      difficultyLevel = difficultyLevel,
//      board = board.map(Field::toDto),
//      playerMark = playerMark,
//      aiMark = aiMark,
//      status = status,
//      playerWins = playerWins,
//      playerDefeats = playerDefeats,
//      draws = draws
//  )
//
//  companion object {
//    fun fromDto(dto: GameDto) = Game(
//        gameId = dto.gameId,
//        difficultyLevel = dto.difficultyLevel,
//        board = dto.board.map(Field.Companion::fromDto),
//        playerMark = dto.playerMark,
//        aiMark = dto.aiMark,
//        status = dto.status,
//        playerWins = dto.playerWins,
//        playerDefeats = dto.playerDefeats,
//        draws = dto.draws
//    )
//  }
}