package com.moonlightbutterfly.makao

data class Card (
    val value: CardValue,
    val type: CardType,
    var highlighted: Boolean = false
)