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
class OAuth2ServerConfig @Autowired constructor(
  private val authenticationManager: AuthenticationManager,
  private val passwordEncoder: PasswordEncoder,
  private val oath2Properties: Oath2Properties,
  private val oAuth2ResponseExceptionTranslator: OAuth2ResponseExceptionTranslator,
  private val dataSource: DataSource
) : AuthorizationServerConfigurerAdapter() {

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
    const val GRANT_TYPE_PASSWORD = "password"
    private const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    private const val GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials"

    private const val SCOPE_READ = "read"
    private const val SCOPE_WRITE = "write"

    private const val ACCESS_TOKEN_VALIDITY_SECONDS = 180
    private const val REFRESH_TOKEN_VALIDITY_SECONDS = 300
  }
}
