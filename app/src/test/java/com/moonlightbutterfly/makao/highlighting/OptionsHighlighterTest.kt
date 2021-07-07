package com.moonlightbutterfly.makao.highlighting

import com.moonlightbutterfly.makao.Rank
import com.moonlightbutterfly.makao.Suit
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.effect.*
import org.junit.Test

import org.junit.Assert.*

class OptionsHighlighterTest {

    private val highlighter = OptionsHighlighter.instance

    private val hand = listOf(
        Card(Rank.TEN, Suit.CLUBS),
        Card(Rank.TEN, Suit.DIAMONDS),
        Card(Rank.FOUR, Suit.HEARTS),
        Card(Rank.THREE, Suit.SPADES),
        Card(Rank.TEN, Suit.SPADES),
        Card(Rank.QUEEN, Suit.SPADES),
        Card(Rank.ACE, Suit.HEARTS),
        Card(Rank.JACK, Suit.CLUBS),
        Card(Rank.FIVE, Suit.HEARTS),
        Card(Rank.SIX, Suit.DIAMONDS),
        Card(Rank.SEVEN, Suit.CLUBS),
    )

    @Test
    fun `should filter proper cards with regular card`() {
        // GIVEN
        val topCard = Card(Rank.FIVE, Suit.CLUBS)
        val cardsTakenInRound = 0
        val cardWasPlaced = false
        val effect: Effect? = null
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.TEN, Suit.CLUBS),
            Card(Rank.QUEEN, Suit.SPADES),
            Card(Rank.JACK, Suit.CLUBS),
            Card(Rank.FIVE, Suit.HEARTS),
            Card(Rank.SEVEN, Suit.CLUBS),
        ), output.cardsToPlay)
        assertTrue(output.drawPossible)
        assertFalse(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with regular card and card taken and placed`() {
        // GIVEN
        val topCard = Card(Rank.FIVE, Suit.CLUBS)
        val cardsTakenInRound = 1
        val cardWasPlaced = true
        val effect: Effect? = null
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.FIVE, Suit.HEARTS),
        ), output.cardsToPlay)
        assertFalse(output.drawPossible)
        assertTrue(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with three action card`() {
        // GIVEN
        val topCard = Card(Rank.THREE, Suit.CLUBS)
        val cardsTakenInRound = 0
        val cardWasPlaced = false
        val effect: Effect = DrawCardsEffect(THREE)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.THREE, Suit.SPADES),
            Card(Rank.QUEEN, Suit.SPADES),
        ), output.cardsToPlay)
        assertTrue(output.drawPossible)
        assertFalse(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with two action card and cards taken`() {
        // GIVEN
        val topCard = Card(Rank.TWO, Suit.CLUBS)
        val cardsTakenInRound = 3
        val cardWasPlaced = false
        val effect: Effect = DrawCardsEffect(FIVE)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf<Card>(), output.cardsToPlay)
        assertTrue(output.drawPossible)
        assertFalse(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with four action card`() {
        // GIVEN
        val topCard = Card(Rank.FOUR, Suit.CLUBS)
        val cardsTakenInRound = 1
        val cardWasPlaced = false
        val effect: Effect = WaitTurnEffect(1)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.FOUR, Suit.HEARTS),
            Card(Rank.QUEEN, Suit.SPADES),
        ), output.cardsToPlay)
        assertFalse(output.drawPossible)
        assertTrue(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with ace action card`() {
        // GIVEN
        val topCard = Card(Rank.ACE, Suit.SPADES)
        val cardsTakenInRound = 0
        val cardWasPlaced = false
        val effect: Effect = RequireSuitEffect(Suit.CLUBS)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.TEN, Suit.CLUBS),
            Card(Rank.QUEEN, Suit.SPADES),
            Card(Rank.ACE, Suit.HEARTS),
            Card(Rank.JACK, Suit.CLUBS),
            Card(Rank.SEVEN, Suit.CLUBS),
        ), output.cardsToPlay)
        assertTrue(output.drawPossible)
        assertFalse(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with ace action card and card placed`() {
        // GIVEN
        val topCard = Card(Rank.ACE, Suit.SPADES)
        val cardsTakenInRound = 0
        val cardWasPlaced = true
        val effect: Effect = RequireSuitEffect(Suit.CLUBS)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(Card(Rank.ACE, Suit.HEARTS)), output.cardsToPlay)
        assertFalse(output.drawPossible)
        assertTrue(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with jack action card`() {
        // GIVEN
        val topCard = Card(Rank.JACK, Suit.DIAMONDS)
        val cardsTakenInRound = 0
        val cardWasPlaced = false
        val effect: Effect = RequireRankEffect(Rank.SIX)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.QUEEN, Suit.SPADES),
            Card(Rank.JACK, Suit.CLUBS),
            Card(Rank.SIX, Suit.DIAMONDS),
        ), output.cardsToPlay)
        assertTrue(output.drawPossible)
        assertFalse(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with two action card and enough cards taken`() {
        // GIVEN
        val topCard = Card(Rank.TWO, Suit.HEARTS)
        val cardsTakenInRound = 2
        val cardWasPlaced = false
        val effect: Effect = DrawCardsEffect(TWO)
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf<Card>(), output.cardsToPlay)
        assertFalse(output.drawPossible)
        assertTrue(output.finishRoundPossible)
    }

    @Test
    fun `should filter proper cards with regular card 2`() {
        // GIVEN
        val topCard = Card(Rank.TEN, Suit.HEARTS)
        val cardsTakenInRound = 0
        val cardWasPlaced = false
        val effect: Effect? = null
        // WHEN
        val output = highlighter.provideOptionsToHighlight(hand, topCard, cardsTakenInRound, cardWasPlaced, effect)
        // THEN
        assertEquals(listOf(
            Card(Rank.TEN, Suit.CLUBS),
            Card(Rank.TEN, Suit.DIAMONDS),
            Card(Rank.FOUR, Suit.HEARTS),
            Card(Rank.TEN, Suit.SPADES),
            Card(Rank.QUEEN, Suit.SPADES),
            Card(Rank.ACE, Suit.HEARTS),
            Card(Rank.FIVE, Suit.HEARTS),
        ), output.cardsToPlay)
        assertTrue(output.drawPossible)
        assertFalse(output.finishRoundPossible)
    }

    private companion object {
        private const val THREE = 3
        private const val TWO = 2
        private const val FIVE = 5
    }
}