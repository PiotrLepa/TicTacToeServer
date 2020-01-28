package com.piotr.tictactoe.security.config

import com.piotr.tictactoe.security.error.OAuth2ExceptionEntryPoint
import com.piotr.tictactoe.security.error.OAuth2ResponseExceptionTranslator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint

@Configuration
@EnableResourceServer
class ResourceServerConfig : ResourceServerConfigurerAdapter() {

  @Autowired
  private lateinit var oAuth2ResponseExceptionTranslator: OAuth2ResponseExceptionTranslator

  @Autowired
  private lateinit var oAuth2ExceptionEntryPoint: OAuth2ExceptionEntryPoint

  override fun configure(http: HttpSecurity) {
    http.anonymous().disable()
        .requestMatchers()
        .antMatchers("/securetest")
        .and()
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().authenticationEntryPoint(oAuth2ExceptionEntryPoint)
  }

  override fun configure(resources: ResourceServerSecurityConfigurer) {
    val auth2AccessDeniedHandler = OAuth2AccessDeniedHandler().apply {
      setExceptionTranslator(oAuth2ResponseExceptionTranslator)
    }
    val authenticationEntryPoint = OAuth2AuthenticationEntryPoint().apply {
      setExceptionTranslator(oAuth2ResponseExceptionTranslator)
    }
    resources.accessDeniedHandler(auth2AccessDeniedHandler)
        .authenticationEntryPoint(authenticationEntryPoint)
  }
}