package com.moonlightbutterfly.makao

data class HighlightInfo(
    val cardsToPlay: List<Card>,
    val drawPossible: Boolean,
    val finishRoundPossible: Boolean
)