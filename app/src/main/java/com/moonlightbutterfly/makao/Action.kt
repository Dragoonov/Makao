package com.moonlightbutterfly.makao

interface Action

data class DrawCard (val player: Player, val card: Card): Action
data class PlaceCard (val player: Player, val card: Card): Action