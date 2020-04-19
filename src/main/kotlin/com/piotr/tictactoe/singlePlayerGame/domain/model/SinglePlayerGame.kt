package com.piotr.tictactoe.singlePlayerGame.domain.model

import com.piotr.tictactoe.gameMove.domain.model.FieldMark
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "single_player_games")
data class SinglePlayerGame(

  @Column(name = "player_id")
  val playerId: Long,

  @Enumerated(EnumType.STRING)
  val status: SinglePlayerGameStatus,

  @Column(name = "difficulty_level")
  @Enumerated(EnumType.STRING)
  val difficultyLevel: DifficultyLevel,

  @Column(name = "player_mark")
  @Enumerated(EnumType.STRING)
  val playerMark: FieldMark,

  @Column(name = "computer_mark")
  @Enumerated(EnumType.STRING)
  val computerMark: FieldMark,

  @Column(name = "creation_date")
  @CreationTimestamp
  val creationDate: Timestamp = Timestamp(0),

  @Column(name = "modification_date")
  @UpdateTimestamp
  val modificationDate: Timestamp = Timestamp(0),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val gameId: Long? = null
) {

  constructor() : this(-1, SinglePlayerGameStatus.ON_GOING,
      DifficultyLevel.EASY, FieldMark.X, FieldMark.O
  )
}