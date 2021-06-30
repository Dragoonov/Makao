package com.moonlightbutterfly.makao

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import com.moonlightbutterfly.makao.ui.Utils

fun ImageView.moveAnimation(
    sourceX: Float = this.x,
    sourceY: Float = this.y,
    targetX: Float = this.x,
    targetY: Float = this.y,
    durationAnim: Long = 1000,
    ignoreX: Boolean = false,
    ignoreY: Boolean = false
): Animator = this.animation(sourceX, sourceY, targetX, targetY, durationAnim, ignoreX, ignoreY).apply {
}

fun ImageView.scaleAnimation(
    sourceX: Float = this.scaleX,
    sourceY: Float = this.scaleY,
    targetX: Float = this.scaleX,
    targetY: Float = this.scaleY,
    durationAnim: Long = 1000,
    ignoreY: Boolean = false,
    ignoreX: Boolean = false
): Animator = this.animation(sourceX, sourceY, targetX, targetY, durationAnim, ignoreX, ignoreY, "scale")

private fun ImageView.animation(
    sourceX: Float,
    sourceY: Float,
    targetX: Float,
    targetY: Float,
    durationAnim: Long = 1000,
    ignoreX: Boolean,
    ignoreY: Boolean,
    property: String = ""
): ObjectAnimator {
    val y = PropertyValuesHolder.ofFloat(if (property.isEmpty()) "y" else "${property}Y", sourceY, targetY)
    val x = PropertyValuesHolder.ofFloat(if (property.isEmpty()) "x" else "${property}X", sourceX, targetX)
    return when {
        ignoreX && ignoreY -> error("Cannot ignore two properties at the same time")
        ignoreX -> ObjectAnimator.ofPropertyValuesHolder(this, y).apply {
            duration = durationAnim
        }
        ignoreY -> ObjectAnimator.ofPropertyValuesHolder(this, x).apply {
            duration = durationAnim
        }

        else -> ObjectAnimator.ofPropertyValuesHolder(this, x, y).apply {
            duration = durationAnim
        }
    }
}

fun ImageView.moveAndScaleAndFlipAnimation(
    card: ImageView,
    durationAnim: Long = 1000,
    targetDrawable: Drawable,
): Animator {
    val animator = this.alignWithTopCardAnimation(card, durationAnim) as AnimatorSet
    val flipX = PropertyValuesHolder.ofFloat("rotationX", 0f, 90f)
    val flipX2 = PropertyValuesHolder.ofFloat("rotationX", 90f, 180f)
    val flip1Anim = ObjectAnimator.ofPropertyValuesHolder(this, flipX).apply {
        duration = 250
        doOnEnd {
            this@moveAndScaleAndFlipAnimation.setImageDrawable(targetDrawable)
            this@moveAndScaleAndFlipAnimation.rotationY = 180f
        }
    }
    val flip2Anim = ObjectAnimator.ofPropertyValuesHolder(this, flipX2).apply {
        duration = 750
    }
    return animator.apply {
        play(flip2Anim).after(flip1Anim)
    }
}

fun ImageView.alignWithTopCardAnimation(targetCard: ImageView, durationAnim: Long = 250): Animator {
    val moveAnimation = moveAnimation(
        targetX = targetCard.x + targetCard.width / 2 - this.width / 2,
        targetY = targetCard.y + targetCard.height / 2 - this.height / 2,
        durationAnim = durationAnim
    )
    val scaleAnimation = scaleAnimation(
        targetX = targetCard.width.toFloat() / this.width,
        targetY = targetCard.height.toFloat() / this.height,
        durationAnim = durationAnim
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
    }
}

fun ImageView.drawAnimation(
    source: ImageView,
    target: ImageView,
    distance: Float,
): Animator {
    val moveAnimation = moveAnimation(
        sourceX = source.x,
        sourceY = source.y,
        targetX = target.x + distance,
        targetY = target.y
    )
    val scaleAnimation = scaleAnimation(
        sourceX = source.width.toFloat() / target.width,
        sourceY = source.height.toFloat() / target.height,
        targetX = 1f,
        targetY = 1f
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
    }
}

fun ImageView.move(value: Float, operation: (Float, Float) -> Float): Animator {
    val source = y
    val target = operation(y, Utils.dpToPixel(value, context))
    return this.translateY(source, target)
}

fun ImageView.translateY(source: Float, target: Float, onEnd: () -> Unit = {}): Animator {
    val translationY =
        PropertyValuesHolder.ofFloat("y", source, target)
    val moveAnim = ObjectAnimator.ofPropertyValuesHolder(this, translationY).apply {
        duration = 1000
        doOnEnd { onEnd() }
    }
    return AnimatorSet().apply {
        play(moveAnim)
    }
}

fun ImageView.initializeTopCard(
    source: ImageView,
    target: ImageView,
): Animator {
    val moveAnimation = moveAnimation(
        sourceX = source.x,
        targetX = target.x + target.width / 2 - source.width / 2,
        sourceY = source.y,
        targetY = target.y + target.height / 2 - source.height / 2
    )
    val scaleAnimation = scaleAnimation(
        sourceX = source.scaleX,
        targetX = target.width.toFloat() / source.width,
        sourceY = source.scaleY,
        targetY = target.height.toFloat() / source.height
    )
    return AnimatorSet().apply {
        playTogether(moveAnimation, scaleAnimation)
    }
}

fun View.lock(lock: Boolean) {
    isEnabled = lock.not()
    if (this is ViewGroup) children.forEach { it.lock(lock) }
}
