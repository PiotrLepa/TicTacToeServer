package com.piotr.tictactoe.core

import com.piotr.tictactoe.core.locale.LocaleSaverInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class WebMvcConfig @Autowired constructor(
  private val localeChangeInterceptor: LocaleChangeInterceptor,
  private val localeSaverInterceptor: LocaleSaverInterceptor
) : WebMvcConfigurerAdapter() {

  override fun addInterceptors(registry: InterceptorRegistry) {
    with(registry) {
      addInterceptor(localeChangeInterceptor)
      addInterceptor(localeSaverInterceptor)
    }
  }
}