package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.TimeInterpolator
import java.util.ArrayList

class LazyAnimator(
    target: Any,
    property: String,
    private val sourceX: () -> Float,
    private val sourceY: () -> Float,
    private val targetX: () -> Float,
    private val targetY: () -> Float): Animator() {

    private val x: PropertyValuesHolder = PropertyValuesHolder.ofFloat(if (property.isEmpty()) "x" else property + "X", sourceX(), targetX())
    private val y: PropertyValuesHolder = PropertyValuesHolder.ofFloat(if (property.isEmpty()) "y" else property + "Y", sourceY(), targetY())
    private val objectAnimator = ObjectAnimator.ofPropertyValuesHolder(target, x, y)

    override fun getStartDelay(): Long = objectAnimator.startDelay

    override fun setStartDelay(startDelay: Long) = run { objectAnimator.startDelay = startDelay }

    override fun setDuration(duration: Long): Animator {
        objectAnimator.duration = duration
        return objectAnimator
    }

    override fun getDuration() = objectAnimator.duration

    override fun setInterpolator(value: TimeInterpolator?) = run { objectAnimator.interpolator = value }

    override fun isRunning() = objectAnimator.isRunning

    override fun addListener(listener: AnimatorListener?) = objectAnimator.addListener(listener)

    override fun end() = objectAnimator.end()

    override fun cancel() {
        objectAnimator.cancel()
    }

    override fun addPauseListener(listener: AnimatorPauseListener?) {
        objectAnimator.addPauseListener(listener)
    }

    override fun clone(): Animator = objectAnimator.clone()

    override fun getInterpolator(): TimeInterpolator {
        return objectAnimator.interpolator
    }

    override fun getListeners(): ArrayList<AnimatorListener> {
        return objectAnimator.listeners
    }

    override fun getTotalDuration(): Long {
        return objectAnimator.totalDuration
    }

    override fun isPaused(): Boolean {
        return objectAnimator.isPaused
    }

    override fun isStarted(): Boolean {
        return objectAnimator.isStarted
    }

    override fun pause() {
        objectAnimator.pause()
    }

    override fun removeAllListeners() {
        objectAnimator.removeAllListeners()
    }

    override fun removeListener(listener: AnimatorListener?) {
        objectAnimator.removeListener(listener)
    }

    override fun removePauseListener(listener: AnimatorPauseListener?) {
        objectAnimator.removePauseListener(listener)
    }

    override fun resume() {
        objectAnimator.resume()
    }

    override fun setTarget(target: Any?) {
        objectAnimator.target = target
    }

    override fun setupEndValues() {
        objectAnimator.setupEndValues()
    }

    override fun setupStartValues() {
        objectAnimator.setupStartValues()
    }

    override fun start() {
        x.setFloatValues(sourceX(), targetX())
        y.setFloatValues(sourceY(), targetY())
        objectAnimator.setValues(x, y)
        objectAnimator.start()
        super.start()
    }

}