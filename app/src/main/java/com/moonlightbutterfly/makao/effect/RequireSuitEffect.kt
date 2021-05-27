package com.moonlightbutterfly.makao.effect

import com.moonlightbutterfly.makao.Suit

class RequireSuitEffect (private var suit: Suit): Effect {
    fun getSuit() = suit
    fun setSuit(suit: Suit) {
        this.suit = suit
    }

    override fun merge(effect: Effect): Effect = effect
}