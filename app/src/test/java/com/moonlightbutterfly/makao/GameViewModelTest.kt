package com.moonlightbutterfly.makao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.CardWrapper
import com.moonlightbutterfly.makao.dataclasses.HighlightInfo
import com.moonlightbutterfly.makao.effect.Effect
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class GameViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val highlightedCards = listOf(Card(Rank.ACE, Suit.CLUBS), Card(Rank.EIGHT, Suit.DIAMONDS))
    private var drawPossible = false
    private var canFinish = true

    private val optionsHighlighter: OptionsHighlighter = mockk {
        every {
            provideOptionsToHighlight(any(), any(), any(), any(), any())
        } returns HighlightInfo(highlightedCards, drawPossible, canFinish)
    }

    private val gameProvider: (List<String>) -> Game = mockk()
    private val action = mockk<Action>()
    private val action2 = mockk<Action>()

    private val game = mockk<Game>(relaxed = true) {
        every { drawCard(any()) } returns action
        every { startGame() } returns listOf(action, action2)
        every { nextTurn(any()) } returns listOf(action, action2)
    }

    private lateinit var viewModel: GameViewModel

    @Before
    fun setup() {
        every { gameProvider(any()) } returns game
        viewModel = GameViewModel(optionsHighlighter, gameProvider)
    }

    @Test
    fun `should calculate proper values on animation ended`() {
        // GIVEN WHEN
        viewModel.onAnimationsEnded()
        // THEN
        assertEquals(highlightedCards, viewModel.possibleMoves.value)
        assertEquals(drawPossible, viewModel.drawPossible.value)
        assertEquals(canFinish, viewModel.finishRoundPossible.value)
    }

    @Test
    fun `should calculate proper values on card placed on top`() {
        // GIVEN
        val card = mockk<Card>()
        val effect = mockk<Effect>()
        val cardWrapper = CardWrapper(card, mockk(), true)
        // WHEN
        viewModel.onCardPlacedOnTop(cardWrapper, effect)
        // THEN
        assertEquals(highlightedCards, viewModel.possibleMoves.value)
        assertEquals(drawPossible, viewModel.drawPossible.value)
        assertEquals(canFinish, viewModel.finishRoundPossible.value)
        verify {
            game.placeCardOnTop(any(), card, effect)
        }
    }

    @Test
    fun `should return one action on drawn card`() {
        // GIVEN WHEN
        viewModel.onDrawnCard()
        // THEN
        assertEquals(ONE, viewModel.actionsToPerform.value?.size)
        assertEquals(action, viewModel.actionsToPerform.value?.get(0))
    }

    @Test
    fun `should return two mocked actions on start game`() {
        // GIVEN WHEN
        viewModel.onStartGame()
        // THEN
        assertEquals(TWO, viewModel.actionsToPerform.value?.size)
        assertEquals(listOf(action, action2), viewModel.actionsToPerform.value)
    }

    @Test
    fun `should return proper amount of actions on turn finished`() {
        // GIVEN WHEN
        viewModel.onTurnFinished()
        // THEN
        assertEquals(EIGHT, viewModel.actionsToPerform.value?.size)
    }

    private companion object {
        private const val EIGHT = 8
        private const val ONE = 1
        private const val TWO = 2
    }
}