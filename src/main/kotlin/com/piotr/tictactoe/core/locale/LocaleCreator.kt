package com.piotr.tictactoe.core.locale

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import java.util.Locale

@Configuration
class LocaleCreator {

  @Bean
  fun localeResolver(): LocaleResolver =
      AcceptHeaderLocaleResolver().apply {
        defaultLocale = DEFAULT_LOCALE
      }

  @Bean
  fun localeChangeInterceptor(): LocaleChangeInterceptor = LocaleChangeInterceptor()

  @Bean
  fun messageSource(): MessageSource = ResourceBundleMessageSource()
      .apply {
        setBasename("i18n/messages")
        setDefaultEncoding("UTF-8")
        setFallbackToSystemLocale(false)
        setDefaultLocale(DEFAULT_LOCALE)
      }

  companion object {
    private val DEFAULT_LOCALE = Locale.ENGLISH
  }
}