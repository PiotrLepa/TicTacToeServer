package com.piotr.tictactoe.move.domain.model

import com.piotr.tictactoe.move.dto.MoveDto
import org.joda.time.DateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "moves")
data class Move(

  @Column(name = "game_id")
  var gameId: Long,

  @Column(name = "field_index")
  var fieldIndex: Int,

  var counter: Int,

  @Enumerated(EnumType.STRING)
  var mark: FieldMark,

  @Column(name = "creation_date")
  var creationDate: Long,

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id: Long? = null
) {

  constructor() : this(-1, -1, -1, FieldMark.X, DateTime.now().millis)

  fun toDto() = MoveDto(
      id = id!!,
      gameId = gameId,
      fieldIndex = fieldIndex,
      counter = counter,
      mark = mark
  )
}