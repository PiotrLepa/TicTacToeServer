package com.piotr.tictactoe.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class JwtTokenUtil : Serializable {

  @Autowired
  private lateinit var jwtProperties: JwtProperties

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