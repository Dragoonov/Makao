package com.moonlightbutterfly.makao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {

    private val john = Player("John", mutableListOf())
    private val arthur = Player("Arthur", mutableListOf())
    private val game = Game(listOf(john, arthur))

    private val actions: Map<Player, (action: Action) -> Unit> = mapOf(
        john to {
            when (it) {
                is DrawCard -> _johnDrawCard.postValue(it.card)
                is PlaceCard -> _johnPlaceCard.postValue(it.card)
                is Win -> _johnWon.postValue(true)
            }
        },
        arthur to {
            when (it) {
                is DrawCard -> _arthurDrawCard.postValue(it.card)
                is PlaceCard -> _arthurPlaceCard.postValue(it.card)
                is Win -> _arthurWon.postValue(true)
            }
        }
    )

    private val _johnDrawCard = MutableLiveData<Card>()
    val johnDrawCard: LiveData<Card> = _johnDrawCard

    private val _arthurDrawCard = MutableLiveData<Card>()
    val arthurDrawCard: LiveData<Card> = _arthurDrawCard

    private val _johnPlaceCard = MutableLiveData<Card>()
    val johnPlaceCard: LiveData<Card> = _johnPlaceCard

    private val _arthurPlaceCard = MutableLiveData<Card>()
    val arthurPlaceCard: LiveData<Card> = _arthurPlaceCard

    private val _switchPlayerTurn = MutableLiveData<Boolean>()
    val switchPlayerTurn: LiveData<Boolean> = _switchPlayerTurn

    private val _possibleMoves = MutableLiveData<List<Card>>()
    val possibleMoves: LiveData<List<Card>> = _possibleMoves

    private val _drawPossible = MutableLiveData<Boolean>()
    val drawPossible: LiveData<Boolean> = _drawPossible

    private val _johnWon = MutableLiveData<Boolean>()
    val johnWon: LiveData<Boolean> = _johnWon

    private val _arthurWon = MutableLiveData<Boolean>()
    val arthurWon: LiveData<Boolean> = _arthurWon

    fun onCardPlacedOnTop(card: Card) {

    }

    fun onDrawnCard(card: Card) {

    }

    fun onRoundFinished() {
        _switchPlayerTurn.value = false
        val (player, receivedActions) = game.nextTurn()
        receivedActions.forEach { action ->
            actions[player]?.let { it(action) }
        }
        _switchPlayerTurn.value = true
    }
}