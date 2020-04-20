package com.piotr.tictactoe.core.security.error

import com.fasterxml.jackson.databind.ObjectMapper
import com.piotr.tictactoe.core.exception.ErrorResponse
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class OAuth2ExceptionEntryPoint : AuthenticationEntryPoint {

  override fun commence(
    request: HttpServletRequest,
    response: HttpServletResponse,
    arg2: AuthenticationException
  ) {
    with(response) {
      contentType = "application/json"
      status = HttpServletResponse.SC_UNAUTHORIZED
    }

    val errorResponse = ErrorResponse(
        code = HttpServletResponse.SC_UNAUTHORIZED,
        exception = arg2
    )

    ObjectMapper().writeValue(response.outputStream, errorResponse)
  }
}