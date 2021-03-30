package com.moonlightbutterfly.makao

interface Action

data class DrawCard (val card: Card): Action
data class PlaceCard (val card: Card): Action
class Win: Action