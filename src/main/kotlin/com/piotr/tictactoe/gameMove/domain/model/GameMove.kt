package com.piotr.tictactoe.gameMove.domain.model

import org.hibernate.annotations.CreationTimestamp
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
@Table(name = "game_moves")
data class GameMove(

  @Column(name = "game_id")
  val gameId: Long,

  @Column(name = "field_index")
  val fieldIndex: Int,

  val counter: Int,

  @Enumerated(EnumType.STRING)
  val mark: FieldMark,

  @Column(name = "creation_date")
  @CreationTimestamp
  val creationDate: Timestamp = Timestamp(0),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val moveId: Long? = null
) {

  constructor() : this(-1, -1, -1, FieldMark.X)
}