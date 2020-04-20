package com.piotr.tictactoe.user.domain.model

import org.hibernate.annotations.CreationTimestamp
import java.sql.Timestamp
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
  val email: String,

  val username: String,

  val password: String,

  @Column(name = "game_code")
  val playerCode: String,

  @Column(name = "device_token")
  val deviceToken: String,

  @Column(name = "creation_date")
  @CreationTimestamp
  val creationDate: Timestamp = Timestamp(0),

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long? = null
) {

  constructor() : this("", "", "", "", "")
}