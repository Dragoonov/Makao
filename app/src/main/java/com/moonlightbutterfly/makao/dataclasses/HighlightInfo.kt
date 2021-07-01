package com.moonlightbutterfly.makao.dataclasses

data class HighlightInfo(
    val cardsToPlay: List<Card>,
    val drawPossible: Boolean,
    val finishRoundPossible: Boolean
)