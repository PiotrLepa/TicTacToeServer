package com.piotr.tictactoe.core.converter

interface Converter1<T, S, P1> {

  fun convert(from: T, param1: P1): S
}