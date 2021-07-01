package com.moonlightbutterfly.makao

import com.moonlightbutterfly.makao.dataclasses.Card
import com.moonlightbutterfly.makao.dataclasses.Player
import com.moonlightbutterfly.makao.effect.Effect

interface Action

data class DrawCardAction (val player: Player, val card: Card): Action
data class PlaceCardAction (val player: Player, val card: Card): Action
data class InitializeCardAction (val card: Card): Action
class ShowInterfaceAction: Action
class HideInterfaceAction: Action
data class ShowEffectAction (val effect: Effect): Action