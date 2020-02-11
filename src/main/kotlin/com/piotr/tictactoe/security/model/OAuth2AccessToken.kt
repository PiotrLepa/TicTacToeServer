//package com.piotr.tictactoe.security.model
//
//import javax.persistence.Column
//import javax.persistence.Entity
//import javax.persistence.GeneratedValue
//import javax.persistence.GenerationType
//import javax.persistence.Id
//import javax.persistence.Lob
//import javax.persistence.Table
//
//@Entity
//@Table(name = "oauth2_access_tokens")
//class OAuth2AccessToken(
//  @Column(name = "token_id")
//  var tokenId: String,
//
//  @Lob
//  @Column(columnDefinition = "mediumblob")
//  var token: ByteArray,
//
//  @Column(name = "authentication_id")
//  val authenticationId: String,
//
//  val username: String,
//
//  @Column(name = "client_id")
//  val clientId: String,
//
//  @Lob
//  @Column(columnDefinition = "mediumblob")
//  val authentication: ByteArray,
//
//  @Column(name = "refresh_token")
//  val refreshToken: String,
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  val id: Long? = null
//) {
//
//  constructor() : this("", byteArrayOf(), "", "", "", byteArrayOf(), "")
//}