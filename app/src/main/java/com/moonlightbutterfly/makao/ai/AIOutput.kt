package com.moonlightbutterfly.makao.ai

import com.moonlightbutterfly.makao.Action
import com.moonlightbutterfly.makao.dataclasses.BoardState
import com.moonlightbutterfly.makao.dataclasses.Player

data class AIOutput(val boardState: BoardState, val actions: List<Action>, val player: Player)