package com.moonlightbutterfly.makao.effect

class WaitTurnEffect(private val turnsNumber: Int) : Effect {
    operator fun plus (effect: WaitTurnEffect) = WaitTurnEffect(this.turnsNumber + effect.turnsNumber)
    operator fun minus (effect: WaitTurnEffect) = WaitTurnEffect(this.turnsNumber - effect.turnsNumber)
    fun getTurnsNumber() = turnsNumber
}