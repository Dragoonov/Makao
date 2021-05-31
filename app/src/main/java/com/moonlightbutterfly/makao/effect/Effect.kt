package com.moonlightbutterfly.makao.effect

interface Effect {
    fun merge(effect: Effect?): Effect?
}