package com.piotr.tictactoe.core.locale

import com.piotr.tictactoe.user.domain.UserFacade
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LocaleSaverInterceptor(
  private val userFacade: UserFacade
) : HandlerInterceptor {

  override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
    val locale = LocaleContextHolder.getLocale()
    userFacade.updateUserLocale(locale)
    return true
  }
}