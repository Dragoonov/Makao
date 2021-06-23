package com.moonlightbutterfly.makao.ui

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import com.moonlightbutterfly.makao.CardWrapper

object Utils {

    fun isCardNearCenter(card: ImageView, center: View): Boolean {
        val (width, height) = (card.x + card.width / 2) to (card.y + card.height / 2)
        val isXInside = width > center.x && width < center.x + center.width
        val isYInside = height > center.y && height < center.y + center.height
        return isXInside && isYInside
    }

    fun highlightCard(wrapper: CardWrapper, shouldHighlight: Boolean) {
        wrapper.highlighted = shouldHighlight
        if (shouldHighlight) {
            wrapper.imageView.setColorFilter(Color.argb(100, 255, 255, 0))
        } else {
            wrapper.imageView.setColorFilter(Color.argb(0, 0, 0, 0))
        }
    }

    fun highlightCard(imageView: ImageView, highlight: Boolean) {
        if (highlight) {
            imageView.setColorFilter(Color.argb(100, 255, 255, 0))
        } else {
            imageView.setColorFilter(Color.argb(0, 0, 0, 0))
        }
    }

    fun dpToPixel(dp: Float, context: Context) = dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
}