package com.moonlightbutterfly.makao.effect

class WaitTurnEffect(private var turnsNumber: Int) : Effect {
    private operator fun plus (effect: WaitTurnEffect) = WaitTurnEffect(this.turnsNumber + effect.turnsNumber)
    private operator fun minus (effect: WaitTurnEffect) = WaitTurnEffect(this.turnsNumber - effect.turnsNumber)
    fun getTurnsNumber() = turnsNumber
    fun decreaseTurn() {
        if (turnsNumber > 0) {
            turnsNumber -= 1
        }
    }
    override fun merge(effect: Effect?): Effect? {
        return if (effect is WaitTurnEffect) {
            this + effect
        } else {
            effect
        }
    }
}