package com.moonlightbutterfly.makao.dataclasses

data class Player (
    val name: String,
    var hand: MutableList<Card>
) {
    fun clone(): Player {
        return Player(name.substring(0), hand.take(hand.size).toMutableList())
    }
}