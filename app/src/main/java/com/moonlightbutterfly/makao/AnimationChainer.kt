package com.moonlightbutterfly.makao

import android.animation.Animator
import androidx.core.animation.doOnEnd

class AnimationChainer {

    private lateinit var animations: List<() -> Animator>
    private var nextAnim: Animator? = null
    private var index = 0
    private var doOnEnd: () -> Unit = {}
    private var stopped = false

    fun start(list: List<() -> Animator>, doOnEnd: () -> Unit = {}) {
        stopped = false
        animations = list
        index = 0
        this.doOnEnd = doOnEnd
        provideNextAnim()
    }

    fun stop() {
        stopped = true
    }

    private fun provideNextAnim() {
        if (stopped.not()) {
            if (index < animations.size) {
                nextAnim = animations[index++]()
                nextAnim?.apply {
                    doOnEnd { provideNextAnim() }
                    start()
                }
            } else {
                doOnEnd()
            }
        }
    }
}