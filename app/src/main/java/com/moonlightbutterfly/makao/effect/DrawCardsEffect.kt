package com.moonlightbutterfly.makao.effect

class DrawCardsEffect(private val cardsAmount: Int) : Effect {
    private operator fun plus (effect: DrawCardsEffect) = DrawCardsEffect(this.cardsAmount + effect.cardsAmount)
    private operator fun minus (effect: DrawCardsEffect) = DrawCardsEffect(this.cardsAmount - effect.cardsAmount)
    fun getCardsAmount() = cardsAmount
    override fun merge(effect: Effect?): Effect? {
        return if (effect is DrawCardsEffect) {
            this + effect
        } else {
            effect
        }
    }
}