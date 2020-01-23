package com.piotr.tictactoe.security

import java.io.Serializable

data class JwtResponse(
  val jwttoken: String
) : Serializable {

  companion object {
    const val serialVersionUID = -8091879091924046844L
  }
}