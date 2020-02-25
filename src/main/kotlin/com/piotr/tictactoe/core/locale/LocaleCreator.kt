package com.piotr.tictactoe.core.locale

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
import org.springframework.web.servlet.i18n.SessionLocaleResolver
import java.util.Locale

@Configuration
class LocaleCreator {

  @Bean
  fun localeResolver(): LocaleResolver =
      SessionLocaleResolver().apply {
        setDefaultLocale(DEFAULT_LOCALE)
      }

  @Bean
  fun localeChangeInterceptor(): LocaleChangeInterceptor =
      LocaleChangeInterceptor().apply {
        paramName = LOCALE_PARAM_NAME
      }

  @Bean(name = ["messageSource"])
  fun bundleMessageSource(): ResourceBundleMessageSource = ResourceBundleMessageSource()
      .apply {
        setBasename("messages")
      }

  companion object {
    private val DEFAULT_LOCALE = Locale.ENGLISH
    private const val LOCALE_PARAM_NAME = "lang"
  }
}