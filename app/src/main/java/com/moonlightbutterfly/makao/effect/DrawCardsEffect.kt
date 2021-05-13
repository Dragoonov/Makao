package com.moonlightbutterfly.makao.effect

class DrawCardsEffect(private val cardsAmount: Int) : Effect {
    operator fun plus (effect: DrawCardsEffect) = DrawCardsEffect(this.cardsAmount + effect.cardsAmount)
    operator fun minus (effect: DrawCardsEffect) = DrawCardsEffect(this.cardsAmount - effect.cardsAmount)
    fun getCardsAmount() = cardsAmount
}