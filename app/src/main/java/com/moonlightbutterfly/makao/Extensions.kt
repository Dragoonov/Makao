package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.children

private fun ImageView.animation(
    property: String = "",
    sourceX: Float = this.x,
    sourceY: Float = this.y,
    targetX: Float = this.x,
    targetY: Float = this.y,
    durationAnim: Long = 1000
): Animator {
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

fun ImageView.moveBack(initialX: Float, initialY: Float): Animator {
    val animation = this.animation(targetX = initialX, targetY = initialY, durationAnim = 250)
    return AnimatorSet().apply {
        play(animation)
    }
}

fun ImageView.moveX(targetX: Float, durationAnim: Long) {
    val animation = this.animation(targetX = targetX, durationAnim = durationAnim)
    AnimatorSet().apply {
        play(animation)
        start()
    }
}

fun ImageView.placeCardOnTopAnimation(
    card: ImageView,
    durationAnim: Long = 1000,
    targetDrawable: Drawable,
    onEnd: () -> Unit = {}
): Animator {
    val animator = this.align(card, durationAnim) as AnimatorSet
    val flipX = PropertyValuesHolder.ofFloat("rotationX", 0f, 90f)
    val flipX2 = PropertyValuesHolder.ofFloat("rotationX", 90f, 180f)
    val flip2Anim = ObjectAnimator.ofPropertyValuesHolder(this, flipX2).apply {
        duration = 750
    }
    val flip1Anim = ObjectAnimator.ofPropertyValuesHolder(this, flipX).apply {
        duration = 250
        doOnEnd {
            this@placeCardOnTopAnimation.setImageDrawable(targetDrawable)
            this@placeCardOnTopAnimation.rotationY = 180f
        }
    }
    return animator.apply {
        play(flip2Anim).after(flip1Anim)
        doOnEnd { onEnd() }
    }
}

fun ImageView.align(card: ImageView, durationAnim: Long = 250, onEnd: () -> Unit = {}): Animator {
    val moveAnimation = animation(
        sourceX = this.x ,
        targetX = card.x + card.width / 2 - this.width / 2,
        sourceY = this.y ,
        targetY = card.y + card.height / 2 - this.height / 2 ,
        durationAnim = durationAnim
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = this.scaleX,
        targetX = card.width.toFloat() / this.width,
        sourceY = this.scaleY,
        targetY = card.height.toFloat() / this.height,
        durationAnim = durationAnim
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        doOnEnd { onEnd() }
    }
}

fun ImageView.move(value: Float, operation: (Float, Float) -> Float): Animator {
    val source = translationY
    val target = operation(translationY, dpToPixel(value, context))
    return this.translateY(source, target)
}

fun ImageView.translateY(source: Float, target: Float, onEnd: () -> Unit = {}): Animator {
    val translationY =
        PropertyValuesHolder.ofFloat("translationY", source, target)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, translationY).apply {
        duration = 1000
        doOnEnd { onEnd() }
    }
    return AnimatorSet().apply {
        play(moveAnim)
    }
}

fun ImageView.drawAnimation(
    source: ImageView,
    target: ImageView,
    distance: Float,
    onEnd: () -> Unit = {}
): Animator {
    val moveAnimation = animation(
        sourceX = source.x,
        sourceY = source.y,
        targetX = target.x + distance,
        targetY = target.y
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = source.width.toFloat() / target.width,
        sourceY = source.height.toFloat() / target.height,
        targetX = 1f,
        targetY = 1f
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        doOnEnd { onEnd() }
    }
}

fun ImageView.initializeTopCard(
    source: ImageView,
    target: ImageView,
    onEnd: () -> Unit = {}
): Animator {
    val moveAnimation = animation(
        sourceX = source.x,
        targetX = target.x + target.width / 2 - source.width / 2,
        sourceY = source.y,
        targetY = target.y + target.height / 2 - source.height / 2
    )
    val scaleAnimation = animation(
        property = "scale",
        sourceX = source.scaleX,
        targetX = target.width.toFloat() / source.width,
        sourceY = source.scaleY,
        targetY = target.height.toFloat() / source.height
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
        doOnEnd { onEnd() }
    }
}

fun dpToPixel(dp: Float, context: Context) =
    dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT

fun View.lock(lock: Boolean) {
    isEnabled = lock.not()
    if (this is ViewGroup) children.forEach { it.lock(lock) }
}
