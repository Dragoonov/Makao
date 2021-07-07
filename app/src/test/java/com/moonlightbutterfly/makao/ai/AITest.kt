package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.DrawCardAction
import com.moonlightbutterfly.makao.PlaceCardAction
import com.moonlightbutterfly.makao.Rank
import com.moonlightbutterfly.makao.Suit
import com.moonlightbutterfly.makao.dataclasses.BoardState
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.HighlightInfo
import com.moonlightbutterfly.makao.dataclasses.Player
import com.moonlightbutterfly.makao.effect.Effect
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class AITest {

    private val highlighter: OptionsHighlighter = mockk()
    private val effectProvider: (Card) -> Effect? = mockk()
    private val ai = AI(effectProvider, highlighter)
    private val boardState = BoardState(
        deck = mutableListOf(Card(Rank.SIX, Suit.CLUBS)),
        topStack = mutableListOf(Card(Rank.SIX, Suit.CLUBS)),
        effect = null,
        cardPlacedInRound = false,
        cardsTakenInRound = 0
    )

    @Before
    fun setup() {
        every { effectProvider(any()) } returns mockk(relaxed = true)
    }

    @Test
    fun `get actions for player with cards to place`() {
        // GIVEN
        every { highlighter.provideOptionsToHighlight(any(), any(), any(), any(), any()) } returns HighlightInfo(
            listOf(Card(Rank.QUEEN, Suit.CLUBS)),
            drawPossible = true,
            finishRoundPossible = true
        ) andThen HighlightInfo(
            listOf(),
            drawPossible = true,
            finishRoundPossible = true
        )
        // WHEN
        val results = ai.getActionsForPlayer(
            mockk(relaxed = true),
            boardState
        )
        // THEN
        assertTrue(results.actions.size == 1)
        assertTrue(results.actions[0] is PlaceCardAction)
        assertTrue(results.boardState.cardPlacedInRound)
        assertEquals(0, results.boardState.cardsTakenInRound)
    }
    @Test
    fun `get actions without cards to place`() {
        // GIVEN
        every { highlighter.provideOptionsToHighlight(any(), any(), any(), any(), any()) } returns HighlightInfo(
            listOf(),
            drawPossible = true,
            finishRoundPossible = true
        )
        // WHEN
        val results = ai.getActionsForPlayer(
            mockk(relaxed = true),
            boardState
        )
        // THEN
        assertTrue(results.actions.isEmpty())
        assertFalse(results.boardState.cardPlacedInRound)
        assertEquals(0, results.boardState.cardsTakenInRound)
    }

    @Test
    fun getActionsForPlayer() {
        // GIVEN
        every { highlighter.provideOptionsToHighlight(any(), any(), any(), any(), any()) } returns HighlightInfo(
            listOf(),
            drawPossible = true,
            finishRoundPossible = false
        ) andThen HighlightInfo(
            listOf(),
            drawPossible = true,
            finishRoundPossible = true
        )
        // WHEN
        val results = ai.getActionsForPlayer(
            mockk(relaxed = true),
            boardState
        )
        // THEN
        assertTrue(results.actions.size == 1)
        assertTrue(results.actions[0] is DrawCardAction)
        assertFalse(results.boardState.cardPlacedInRound)
        assertEquals(1, results.boardState.cardsTakenInRound)
    }
}