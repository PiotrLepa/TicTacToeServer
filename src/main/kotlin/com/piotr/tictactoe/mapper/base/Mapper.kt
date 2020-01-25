package com.piotr.tictactoe.mapper.base

interface Mapper<RQ, RS, M> {

  fun fromRequest(request: RQ): M = throw IllegalStateException()

  fun toResponse(model: M): RS = throw IllegalStateException()
}