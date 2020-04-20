package com.piotr.tictactoe.core.converter

interface Converter2<T, S, P1, P2> {

  fun convert(from: T, param1: P1, param2: P2): S
}