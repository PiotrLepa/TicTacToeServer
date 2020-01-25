package com.piotr.tictactoe.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import javax.servlet.http.HttpServletRequest

object UserContext {

  fun authenticateUser(userDetails: UserDetails, request: HttpServletRequest) {
    SecurityContextHolder.getContext().authentication =
        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
            .apply {
              details = WebAuthenticationDetailsSource().buildDetails(request)
            }
  }

  fun getAuthenticatedUserEmail(): String = SecurityContextHolder.getContext().authentication.name
}