package com.moonlightbutterfly.makao

import com.moonlightbutterfly.makao.ai.AI
import com.moonlightbutterfly.makao.ai.AIOutput
import com.moonlightbutterfly.makao.dataclasses.BoardState
import com.moonlightbutterfly.makao.dataclasses.Player
import com.moonlightbutterfly.makao.effect.Effect
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class GameTest {
    private val player1 = "mock1"
    private val player2 = "mock2"
    private val player3 = "mock3"
    private var initializedActions: List<Action>? = null
    private val mockEffect = mockk<Effect>()
    private var effect: Effect? = mockEffect
    private val effectListener: ((Effect?) -> Unit) = { effect = it }
    private val boardState: BoardState = mockk(relaxed = true)
    private val actions: List<Action> = mockk()
    private val aiPlayer: Player = mockk(relaxed = true)
    private val ai: AI = mockk {
        every { getActionsForPlayer(any(),any()) } returns AIOutput(boardState, actions, aiPlayer)
    }
    private val game = Game(
        listOf(player1, player2, player3)
    ) { ai }

    @Before
    fun initialize() {
        initializedActions = game.startGame()
        game.setOnEffectListener(effectListener)
    }

    @Test
    fun `players should have cards after initialization`() {
        // GIVEN WHEN
        // THEN
        val player1Hand = game.getPlayerHand(player1)
        val player2Hand = game.getPlayerHand(player2)
        val player3Hand = game.getPlayerHand(player3)
        assertEquals(FIVE, player1Hand.size)
        assertEquals(FIVE, player2Hand.size)
        assertEquals(FIVE, player3Hand.size)
        assertEquals(SIXTEEN, initializedActions?.size)
    }

    @Test
    fun `should take cards and add them to player hand`() {
        // GIVEN
        game.getTopCard()
        // WHEN
        game.drawCard(player1)
        game.drawCard(player2)
        // THEN
        assertEquals(SIX, game.getPlayerHand(player1).size)
        assertEquals(SIX, game.getPlayerHand(player2).size)
    }

    @Test
    fun `should count cards taken in round`() {
        // GIVEN WHEN
        repeat(SIX) {
            game.drawCard(player1)
        }
        // THEN
        assertEquals(SIX, game.getCardsTakenInRound())
    }

    @Test
    fun `should place player's card to top`() {
        // GIVEN
        val topCard = game.getTopCard()
        game.drawCard(player1)
        val card = game.getPlayerHand(player1).last()
        // WHEN
        game.placeCardOnTop(player1, card)
        // THEN
        assertNotEquals(topCard, card)
        assertEquals(card, game.getTopCard())
        assertEquals(FIVE, game.getPlayerHand(player1).size)
        assertTrue(game.getCardWasPlacedInRound())
        assertNotEquals(mockEffect, effect)
    }

    @Test
    fun `should update state on next turn`() {
        // GIVEN
        game.placeCardOnTop(player1, mockk(relaxed = true))
        game.drawCard(player1)
        assertTrue(game.getCardWasPlacedInRound())
        assertTrue(game.getCardsTakenInRound() > 0)
        // WHEN
        val receivedActions = game.nextTurn()
        // THEN
        assertEquals(actions, receivedActions)
        assertFalse(game.getCardWasPlacedInRound())
        assertTrue(game.getCardsTakenInRound() == 0)
    }

    private companion object {
        private const val FIVE = 5
        private const val SIX = 6
        private const val SIXTEEN = 16
    }
}