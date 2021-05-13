package com.moonlightbutterfly.makao.effect

import com.moonlightbutterfly.makao.Suit

class RequireSuitEffect (private val suit: Suit): Effect {
    fun getSuit() = suit
}