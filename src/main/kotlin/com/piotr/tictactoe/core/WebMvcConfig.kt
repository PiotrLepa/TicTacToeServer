package com.piotr.tictactoe.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor

@Configuration
class WebMvcConfig : WebMvcConfigurerAdapter() {

  @Autowired
  private lateinit var localeChangeInterceptor: LocaleChangeInterceptor

  override fun addInterceptors(registry: InterceptorRegistry) {
    registry.addInterceptor(localeChangeInterceptor)
  }
}