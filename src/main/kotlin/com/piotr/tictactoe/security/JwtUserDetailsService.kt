package com.piotr.tictactoe.security

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class JwtUserDetailsService : UserDetailsService {

  override fun loadUserByUsername(username: String?): UserDetails {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

}