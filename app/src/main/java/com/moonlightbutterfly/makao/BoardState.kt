package com.moonlightbutterfly.makao

import com.moonlightbutterfly.makao.effect.Effect

data class BoardState (
    val deck: MutableList<Card> = mutableListOf(),
    val topStack: MutableList<Card> = mutableListOf(),
    var effect: Effect? = null
)

fun BoardState.clone(): BoardState = BoardState(
    deck.take(deck.size).toMutableList(),
    topStack.take(topStack.size).toMutableList(),
    effect
)