package com.piotr.tictactoe.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.Date

@Component
class JwtTokenUtil : Serializable {

  @Autowired
  private lateinit var jwtTokenProperties: JwtTokenProperties

  fun getUsernameFromToken(token: String): String? =
      getClaimFromToken(token, Claims::getSubject)

  fun validateToken(token: String, userDetails: UserDetails): Boolean =
      getUsernameFromToken(token) == userDetails.username && !isTokenExpired(token)

  // while creating the token -
  // 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
  // 2. Sign the JWT using the HS512 algorithm and secret key.
  // 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
  // compaction of the JWT to a URL-safe string
  fun generateToken(userDetails: UserDetails): String =
      Jwts.builder()
          .setSubject(userDetails.username)
          .setIssuedAt(Date(System.currentTimeMillis()))
          .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
          .signWith(SignatureAlgorithm.HS512, jwtTokenProperties.secretKey)
          .compact()

  private fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T? =
      getAllClaimsFromToken(token)?.let { claimsResolver(it) }

  private fun getExpirationDateFromToken(token: String): Date? =
      getClaimFromToken(token, Claims::getExpiration)

  private fun isTokenExpired(token: String): Boolean =
      getExpirationDateFromToken(token)?.before(Date()) ?: false

  private fun getAllClaimsFromToken(token: String): Claims? =
      try {
        Jwts.parser()
            .setSigningKey(jwtTokenProperties.secretKey)
            .parseClaimsJws(token)
            .body
      } catch (e: Exception) {
        null
      }

  companion object {
    const val JWT_TOKEN_VALIDITY = 5 * 60 * 60.toLong()
  }
}