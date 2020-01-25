package com.piotr.tictactoe.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthRequestFilter : OncePerRequestFilter() {

  @Autowired
  private lateinit var authUserDetailsService: AuthUserDetailsService

  @Autowired
  private lateinit var jwtTokenUtil: JwtTokenUtil

  @Autowired
  private lateinit var jwtProperties: JwtProperties

  override fun doFilterInternal(
    request: HttpServletRequest,
    response: HttpServletResponse,
    chain: FilterChain
  ) {
    val jwtToken = getToken(request)
    if (jwtToken == null) {
      logger.warn("Request doesn't have authorization token")
      return chain.doFilter(request, response)
    }

    val username = jwtTokenUtil.getUsernameFromToken(jwtToken) ?: run {
      logger.warn("Cannot get username from token")
      return chain.doFilter(request, response)
    }

    val userDetails = authUserDetailsService.loadUserByUsername(username) ?: run {
      logger.warn("User not found")
      return chain.doFilter(request, response)
    }

    if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
      SecurityContextHolder.getContext().authentication =
          UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
              .apply {
                details = WebAuthenticationDetailsSource().buildDetails(request)
              }
    }
    chain.doFilter(request, response)
  }

  private fun getToken(request: HttpServletRequest): String? =
      request.getHeader(AUTHORIZATION_HEADER)?.let { header ->
        if (header.startsWith(jwtProperties.tokenPrefix)) {
          header.removePrefix(jwtProperties.tokenPrefix).trim()
        } else {
          null
        }
      }

  companion object {
    private const val AUTHORIZATION_HEADER = "Authorization"
  }
}