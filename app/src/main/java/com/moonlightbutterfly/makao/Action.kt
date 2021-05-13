package com.moonlightbutterfly.makao

interface Action

data class DrawCardAction (val player: Player, val card: Card): Action
data class PlaceCardAction (val player: Player, val card: Card): Action
data class InitializeCardAction (val card: Card): Action