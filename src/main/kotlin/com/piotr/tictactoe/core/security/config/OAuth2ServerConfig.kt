package com.piotr.tictactoe.core.security.config

import com.piotr.tictactoe.core.security.Oath2Properties
import com.piotr.tictactoe.core.security.error.OAuth2ResponseExceptionTranslator
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
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import javax.sql.DataSource

@Configuration
@EnableAuthorizationServer
class OAuth2ServerConfig : AuthorizationServerConfigurerAdapter() {

  @Autowired
  private lateinit var authenticationManager: AuthenticationManager

  @Autowired
  private lateinit var passwordEncoder: PasswordEncoder

  @Autowired
  private lateinit var oath2Properties: Oath2Properties

  @Autowired
  private lateinit var oAuth2ResponseExceptionTranslator: OAuth2ResponseExceptionTranslator

  @Autowired
  private lateinit var dataSource: DataSource

  @Bean
  fun tokenStore(): TokenStore = JdbcTokenStore(dataSource)

  override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
    endpoints.tokenStore(tokenStore())
        .authenticationManager(authenticationManager)
        .exceptionTranslator(oAuth2ResponseExceptionTranslator)
  }

  override fun configure(clients: ClientDetailsServiceConfigurer) {
    clients
        .inMemory()
        .withClient(oath2Properties.clientId)
        .secret(passwordEncoder.encode(oath2Properties.clientSecret))
        .authorizedGrantTypes(
            GRANT_TYPE_CLIENT_CREDENTIALS, // TODO needed?
            GRANT_TYPE_PASSWORD,
            GRANT_TYPE_REFRESH_TOKEN)
        .scopes(SCOPE_READ,
            SCOPE_WRITE)
        .accessTokenValiditySeconds(
            ACCESS_TOKEN_VALIDITY_SECONDS)
        .refreshTokenValiditySeconds(
            REFRESH_TOKEN_VALIDITY_SECONDS)
  }

  companion object {
    private const val GRANT_TYPE_PASSWORD = "password"
    private const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    private const val GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials"

    private const val SCOPE_READ = "read"
    private const val SCOPE_WRITE = "write"

    private const val ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 // 1 hour
    private const val REFRESH_TOKEN_VALIDITY_SECONDS = 24 * 60 * 60 // 1 day
  }
}