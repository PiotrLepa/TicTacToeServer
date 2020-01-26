package com.piotr.tictactoe.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@Configuration
@EnableAuthorizationServer
class OAuth2ServerConfig : AuthorizationServerConfigurerAdapter() {

  @Autowired
  private lateinit var authenticationManager: AuthenticationManager

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  @Autowired
  private lateinit var oathProperties: OathProperties

  override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
    endpoints.tokenStore(tokenStore())
        .authenticationManager(authenticationManager)
  }

//  override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
//    endpoints.authenticationManager(authenticationManager)
//  }

  override fun configure(clients: ClientDetailsServiceConfigurer) {
    clients
        .inMemory()
        .withClient(oathProperties.clientId)
        .secret(passwordEncoder.encode(oathProperties.clientSecret))
        .authorizedGrantTypes("client_credentials", "password")
//        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
        .scopes("read", "write", "trust")
//        .resourceIds("oauth2-resource")
        .accessTokenValiditySeconds(5000)
//        .refreshTokenValiditySeconds(999999999)
  }

  override fun configure(security: AuthorizationServerSecurityConfigurer) {
    security.checkTokenAccess("isAuthenticated()") // TODO needed?
  }

  @Bean
  fun tokenStore(): TokenStore = InMemoryTokenStore()
}