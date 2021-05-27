package com.moonlightbutterfly.makao.effect

class WaitTurnEffect(private val turnsNumber: Int) : Effect {
    private operator fun plus (effect: WaitTurnEffect) = WaitTurnEffect(this.turnsNumber + effect.turnsNumber)
    private operator fun minus (effect: WaitTurnEffect) = WaitTurnEffect(this.turnsNumber - effect.turnsNumber)
    fun getTurnsNumber() = turnsNumber
    override fun merge(effect: Effect): Effect {
        return if (effect is WaitTurnEffect) {
            this + effect
        } else {
            effect
        }
    }
}