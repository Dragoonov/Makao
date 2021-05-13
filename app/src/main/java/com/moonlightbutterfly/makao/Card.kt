package com.moonlightbutterfly.makao

data class Card (
    val rank: Rank,
    val suit: Suit,
    var highlighted: Boolean = false
)

