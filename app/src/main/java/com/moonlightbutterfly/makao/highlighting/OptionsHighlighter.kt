package com.moonlightbutterfly.makao.highlighting

import com.moonlightbutterfly.makao.*
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.HighlightInfo
import com.moonlightbutterfly.makao.effect.*
import com.moonlightbutterfly.makao.utils.CardPeeker


class OptionsHighlighter private constructor() {

    fun provideOptionsToHighlight(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean,
        effect: Effect? = null
    ): HighlightInfo {
        return if (effect == null) {
            provideCardsForNoEffect(hand, topCard, cardsTakenInRound, cardWasPlaced)
        } else {
            when (effect) {
                is DrawCardsEffect -> provideCardsForDrawCardsEffect(
                    hand,
                    topCard,
                    cardsTakenInRound,
                    cardWasPlaced,
                    effect.getCardsAmount()
                )
                is RequireSuitEffect -> provideCardsForRequireSuitEffect(hand, topCard, effect, cardsTakenInRound, cardWasPlaced)
                is RequireRankEffect -> provideCardsForRequireRankEffect(hand, topCard, effect, cardsTakenInRound, cardWasPlaced)
                is WaitTurnEffect -> provideCardsForWaitTurnEffect(hand, cardsTakenInRound, cardWasPlaced)
                else -> error("Wrong effect: $effect")
            }
        }
    }

    private fun provideCardsForNoEffect(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): HighlightInfo {
        return HighlightInfo(
            cardsToPlay = hand.filter {
                when {
                    topCard.rank == Rank.QUEEN && cardWasPlaced.not() -> true

                    cardWasPlaced -> it.rank == topCard.rank

                    else -> it.rank == topCard.rank || it.suit == topCard.suit || (it.rank == Rank.QUEEN)
                }
            },
            drawPossible = ((cardsTakenInRound >= ONE) or cardWasPlaced).not(),
            finishRoundPossible = ((cardsTakenInRound >= ONE) or cardWasPlaced)
        )
    }

    private fun provideCardsForDrawCardsEffect(
        hand: List<Card>,
        topCard: Card,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean,
        cardsAmount: Int
    ): HighlightInfo {
        return HighlightInfo(
            cardsToPlay = if (cardsTakenInRound > ONE) {
                emptyList()
            } else {
                hand.filter {
                    if (cardWasPlaced) {
                        it.rank == topCard.rank
                    } else {
                        val isDrawingCard = it.rank in listOf(Rank.TWO, Rank.THREE, Rank.KING)
                        val isNotNeutralKing = it !in listOf(CardPeeker.KING_OF_CLUBS, CardPeeker.KING_OF_DIAMONDS)
                        (isDrawingCard && (it.suit == topCard.suit || it.rank == topCard.rank) && isNotNeutralKing) || it == CardPeeker.QUEEN_OF_SPADES
                    }
                }
            },
            drawPossible = ((cardsTakenInRound >= cardsAmount) or cardWasPlaced).not(),
            finishRoundPossible = cardWasPlaced or (cardsTakenInRound >= cardsAmount))
    }

    private fun provideCardsForRequireRankEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireRankEffect,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): HighlightInfo {
        return HighlightInfo(
            cardsToPlay = hand.filter {
                if (cardWasPlaced.not()) {
                    it.rank == effect.getRank() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
                } else {
                    it.rank == topCard.rank
                }
            },
            drawPossible = ((cardsTakenInRound >= ONE) or cardWasPlaced).not(),
            finishRoundPossible = (cardsTakenInRound >= ONE) or cardWasPlaced
        )
    }

    private fun provideCardsForRequireSuitEffect(
        hand: List<Card>,
        topCard: Card,
        effect: RequireSuitEffect,
        cardsTakenInRound: Int,
        cardWasPlaced: Boolean
    ): HighlightInfo {
        return HighlightInfo(
            cardsToPlay = hand.filter {
                if (cardWasPlaced.not()) {
                    it.suit == effect.getSuit() || it.rank == topCard.rank || it == CardPeeker.QUEEN_OF_SPADES
                } else {
                    it.rank == topCard.rank
                }
            },
            drawPossible = ((cardsTakenInRound >= ONE) or cardWasPlaced).not(),
            finishRoundPossible = cardWasPlaced or (cardsTakenInRound >= ONE)
        )
    }

    private fun provideCardsForWaitTurnEffect(hand: List<Card>, cardsTakenInRound: Int, cardWasPlaced: Boolean): HighlightInfo {
        return HighlightInfo(
            cardsToPlay = hand.filter { it.rank == Rank.FOUR || it == CardPeeker.QUEEN_OF_SPADES},
            drawPossible = (cardWasPlaced or (cardsTakenInRound >= ONE)).not(),
            finishRoundPossible = (cardsTakenInRound >= ONE) or cardWasPlaced
        )
    }

    companion object {
        val instance = OptionsHighlighter()
        private const val ONE = 1
    }
}