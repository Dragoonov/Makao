package com.moonlightbutterfly.makao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.CardWrapper
import com.moonlightbutterfly.makao.dataclasses.HighlightInfo
import com.moonlightbutterfly.makao.effect.Effect
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
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
//        every { getPlayerHand(any()) } returns mockk()
//        every { getCardWasPlacedInRound() } returns mockk()
//        every { getCardsTakenInRound() } returns 0
//        every { getTopCard() } returns mockk()
//        every { getCurrentEffect() } returns mockk()
    }

    private lateinit var viewModel: GameViewModel

    private val actionsObserver = Observer<List<Action>> {}
    private val cardsObserver = Observer<List<Card>> {}
    private val drawPossiblesObserver = Observer<Boolean> {}
    private val canFinishObserver = Observer<Boolean> {}

    @Before
    fun setup() {
        every { gameProvider(any()) } returns game
        viewModel = GameViewModel(optionsHighlighter, gameProvider)
        viewModel.actionsToPerform.observeForever(actionsObserver)
        viewModel.possibleMoves.observeForever(cardsObserver)
        viewModel.drawPossible.observeForever(drawPossiblesObserver)
        viewModel.finishRoundPossible.observeForever(canFinishObserver)
    }

    @After
    fun tearDown() {
        viewModel.actionsToPerform.removeObserver(actionsObserver)
        viewModel.possibleMoves.removeObserver(cardsObserver)
        viewModel.drawPossible.removeObserver(drawPossiblesObserver)
        viewModel.finishRoundPossible.removeObserver(canFinishObserver)
    }

    @Test
    fun onAnimationsEnded() {
        // GIVEN WHEN
        viewModel.onAnimationsEnded()
        // THEN
        assertEquals(highlightedCards, viewModel.possibleMoves.value)
        assertEquals(drawPossible, viewModel.drawPossible.value)
        assertEquals(canFinish, viewModel.finishRoundPossible.value)
    }

    @Test
    fun onCardPlacedOnTop() {
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
    fun onDrawnCard() {
        // GIVEN WHEN
        viewModel.onDrawnCard()
        // THEN
        assertEquals(1, viewModel.actionsToPerform.value?.size)
        assertEquals(action, viewModel.actionsToPerform.value?.get(0))
    }

    @Test
    fun onStartGame() {
        // GIVEN WHEN
        viewModel.onStartGame()
        // THEN
        assertEquals(2, viewModel.actionsToPerform.value?.size)
        assertEquals(listOf(action, action2), viewModel.actionsToPerform.value)
    }

    @Test
    fun onTurnFinished() {
        // GIVEN WHEN
        viewModel.onTurnFinished()
        // THEN
        assertEquals(8, viewModel.actionsToPerform.value?.size)
    }
}