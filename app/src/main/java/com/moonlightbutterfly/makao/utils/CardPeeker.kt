package com.moonlightbutterfly.makao.utils

import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.Rank
import com.moonlightbutterfly.makao.Suit

class CardPeeker {
    companion object {
        val QUEEN_OF_SPADES = Card(Rank.QUEEN, Suit.SPADES)
        val KING_OF_SPADES = Card(Rank.KING, Suit.SPADES)
        val KING_OF_HEARTS = Card(Rank.KING, Suit.HEARTS)
    }
}