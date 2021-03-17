package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.widget.ImageView

fun ImageView.show() = this.translateY(dpToPixel(-275f, context))
fun ImageView.hide() = this.translateY(dpToPixel(275f, context))
fun ImageView.enemyHide() = this.translateY(dpToPixel(-80f, context))
fun ImageView.enemyShow() = this.translateY(dpToPixel(80f, context))

fun ImageView.animation(
    property: String = "",
    sourceX: Float = this.x,
    sourceY: Float = this.y,
    targetX: Float = this.x,
    targetY: Float = this.y,
    durationAnim: Long = 1000
): ObjectAnimator {
    val x = PropertyValuesHolder.ofFloat(
        if (property.isEmpty()) "x" else property + "X",
        sourceX,
        targetX
    )
    val y = PropertyValuesHolder.ofFloat(
        if (property.isEmpty()) "y" else property + "Y",
        sourceY,
        targetY
    )
    return ObjectAnimator.ofPropertyValuesHolder(this, x, y).apply {
        duration = durationAnim
    }
}

fun ImageView.moveBack(initialX: Float, initialY: Float) {
    val animation = this.animation(targetX = initialX, targetY = initialY, durationAnim = 250)
    AnimatorSet().apply {
        play(animation)
        start()
    }
}

fun ImageView.moveX(targetX: Float) {
    val animation = this.animation(targetX = targetX, durationAnim = 250)
    AnimatorSet().apply {
        play(animation)
        start()
    }
}

fun ImageView.moveToTopStack(card: ImageView, atEnd: () -> Unit) {
    val moveAnimation = animation(
        sourceX = this.x,
        targetX = card.x + card.width / 2 - this.width / 2,
        sourceY = this.y,
        targetY = card.y + card.height / 2 - this.height / 2,
        durationAnim = 250
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = 1f,
        targetX = card.width.toFloat() / this.width,
        sourceY = 1f,
        targetY = card.height.toFloat() / this.height,
        durationAnim = 250
    )
    AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                atEnd()
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}

        })
        start()
    }
}

fun ImageView.highlight(highlight: Boolean) {
    if (highlight) {
        setColorFilter(Color.argb(100, 255, 255, 0))
    } else {
        setColorFilter(Color.argb(0, 0, 0, 0))
    }
}

private fun ImageView.translateY(dp: Float) {
    val translationY =
        PropertyValuesHolder.ofFloat("translationY", this.translationY, this.translationY + dp)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, translationY).apply {
        duration = 1000
    }
    AnimatorSet().apply {
        play(moveAnim)
        start()
    }
}

fun ImageView.animateDrawing(
    source: ImageView,
    target: ImageView,
    distance: Float,
    atEnd: () -> Unit = {}
) {
    val moveAnimation = animation(
        sourceX = source.x,
        targetX = distance,
        sourceY = source.y,
        targetY = target.y
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = source.width.toFloat() / target.width,
        targetX = 1f,
        sourceY = source.height.toFloat() / target.height,
        targetY = 1f
    )
    AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                atEnd()
            }
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
        start()
    }
}

fun ImageView.animateEnemyDrawing(
    source: ImageView,
    target: ImageView,
    distance: Float
) {
    val moveAnimation = animation(
        sourceX = source.x,
        targetX = target.x + distance - target.width,
        sourceY = source.y,
        targetY = target.y
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = source.width.toFloat() / target.width,
        targetX = 1f,
        sourceY = source.height.toFloat() / target.height,
        targetY = 1f
    )
    AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        start()
    }
}

fun dpToPixel(dp: Float, context: Context) =
    dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
