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

  override fun configure(clients: ClientDetailsServiceConfigurer) {
    clients
        .inMemory()
        .withClient("my-trusted-client")
        .secret(passwordEncoder.encode("secret"))
        .authorizedGrantTypes("client_credentials", "password", "refresh_token")
//        .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
        .scopes("read", "write")
//        .resourceIds("oauth2-resource")
        .accessTokenValiditySeconds(5000)
        .refreshTokenValiditySeconds(999999999)
  }

//  /**
//   * We here defines the security constraints on the token endpoint.
//   * We set it up to isAuthenticated, which returns true if the user is not anonymous
//   * @param security the AuthorizationServerSecurityConfigurer.
//   * @throws Exception
//   */
//  override fun configure(security: AuthorizationServerSecurityConfigurer) {
//    security.checkTokenAccess("isAuthenticated()")
//  }

  @Bean
  fun tokenStore(): TokenStore = InMemoryTokenStore()
}