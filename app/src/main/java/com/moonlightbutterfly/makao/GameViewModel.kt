package com.moonlightbutterfly.makao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.CardWrapper
import com.moonlightbutterfly.makao.effect.*
import com.moonlightbutterfly.makao.highlighting.OptionsHighlighter

class GameViewModel(
    private val cardsHighlighter: OptionsHighlighter,
    gameProvider: (list: List<String>) -> Game
) : ViewModel() {

    private var effect: Effect? = null

    private val game = gameProvider(listOf(ARTHUR, JOHN, MAIN_PLAYER)).apply {
        setOnEffectListener { effect = it }
    }

    private val _actionsToPerform = MutableLiveData<List<Action>>()
    val actionsToPerform: LiveData<List<Action>> = _actionsToPerform

    private val _possibleMoves = MutableLiveData<List<Card>>()
    val possibleMoves: LiveData<List<Card>> = _possibleMoves

    private val _drawPossible = MutableLiveData<Boolean>()
    val drawPossible: LiveData<Boolean> = _drawPossible

    private val _finishRoundPossible = MutableLiveData<Boolean>()
    val finishRoundPossible: LiveData<Boolean> = _finishRoundPossible

    fun onAnimationsEnded() {
        updateHighlight()
    }

    fun onCardPlacedOnTop(card: CardWrapper, effect: Effect? = null) {
        game.placeCardOnTop(MAIN_PLAYER, card.card, effect)
        updateHighlight()
    }

    fun onDrawnCard() {
        _actionsToPerform.postValue(listOf(game.drawCard(MAIN_PLAYER)))
    }

    fun onStartGame() {
        _actionsToPerform.postValue(game.startGame())
    }

    fun onTurnFinished() {
        _actionsToPerform.postValue(mutableListOf<Action>().apply {
            add(HideInterfaceAction())
            addAll(getNextTurnsActions())
            add(ShowInterfaceAction())
            if (effect is RequireRankEffect || effect is RequireSuitEffect) {
                add(ShowEffectAction(effect!!))
            }
        })
    }

    private fun getNextTurnsActions(): List<Action> = mutableListOf<Action>().apply {
        for (i in 1..PLAYERS_COUNT) {
            val receivedActions = game.nextTurn(MAIN_PLAYER)
            addAll(receivedActions)
        }
    }

    private fun updateHighlight() {
        val highlightInfo = cardsHighlighter.provideOptionsToHighlight(
            game.getPlayerHand(MAIN_PLAYER),
            game.getTopCard(),
            game.getCardsTakenInRound(),
            game.getCardWasPlacedInRound(),
            game.getCurrentEffect(),
        )
        _possibleMoves.value = highlightInfo.cardsToPlay
        _drawPossible.value = highlightInfo.drawPossible
        _finishRoundPossible.value = highlightInfo.finishRoundPossible
    }

    companion object {
        const val MAIN_PLAYER = "You"
        const val ARTHUR = "Arthur"
        const val JOHN = "John"
        private const val PLAYERS_COUNT = 3
    }
}