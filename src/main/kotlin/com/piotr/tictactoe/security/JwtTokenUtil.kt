package com.piotr.tictactoe.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class JwtTokenUtil : Serializable {

  @Autowired
  private lateinit var jwtProperties: JwtProperties

  fun getEmailFromToken(token: String): String? =
      getClaimFromToken(token, Claims::getSubject)

  fun validateToken(token: String, email: String): Boolean =
      getEmailFromToken(token) == email && !isTokenExpired(token)

  fun generateToken(email: String): String =
      Jwts.builder()
          .setSubject(email)
          .setIssuedAt(DateTime.now().toDate())
          .setExpiration(DateTime.now().plusMinutes(jwtProperties.tokenExpirationMinutes).toDate())
          .signWith(SignatureAlgorithm.HS512, jwtProperties.secretKey)
          .compact()

  private fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T? =
      getAllClaimsFromToken(token)?.let { claimsResolver(it) }

  private fun getExpirationDateFromToken(token: String): DateTime? =
      DateTime(getClaimFromToken(token, Claims::getExpiration))

  private fun isTokenExpired(token: String): Boolean =
      getExpirationDateFromToken(token)?.isBeforeNow ?: false

  private fun getAllClaimsFromToken(token: String): Claims? =
      try {
        Jwts.parser()
            .setSigningKey(jwtProperties.secretKey)
            .parseClaimsJws(token)
            .body
      } catch (e: Exception) {
        null
      }
}