package com.piotr.tictactoe.security.config

import com.piotr.tictactoe.security.error.OAuthExceptionEntryPoint
import com.piotr.tictactoe.security.error.OAuthResponseExceptionTranslator
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
  private lateinit var oAuthResponseExceptionTranslator: OAuthResponseExceptionTranslator

  @Autowired
  private lateinit var oAuthExceptionEntryPoint: OAuthExceptionEntryPoint

  override fun configure(http: HttpSecurity) {
    http.anonymous().disable()
        .requestMatchers()
        .antMatchers("/api/**")
        .and()
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling().authenticationEntryPoint(oAuthExceptionEntryPoint)
  }

  override fun configure(resources: ResourceServerSecurityConfigurer) {
    val auth2AccessDeniedHandler = OAuth2AccessDeniedHandler().apply {
      setExceptionTranslator(oAuthResponseExceptionTranslator)
    }
    val authenticationEntryPoint = OAuth2AuthenticationEntryPoint().apply {
      setExceptionTranslator(oAuthResponseExceptionTranslator)
    }
    resources.accessDeniedHandler(auth2AccessDeniedHandler)
        .authenticationEntryPoint(authenticationEntryPoint)
  }
}