package com.moonlightbutterfly.makao.ui

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import com.moonlightbutterfly.makao.CardWrapper
import com.moonlightbutterfly.makao.R

object Utils {

    fun isCardNearCenter(card: AppCompatImageView, center: View): Boolean {
        val (width, height) = (card.x + card.width / 2) to (card.y + card.height / 2)
        val isXInside = width > center.x && width < center.x + center.width
        val isYInside = height > center.y && height < center.y + center.height
        return isXInside && isYInside
    }

    fun highlightCard(wrapper: CardWrapper, shouldHighlight: Boolean) {
        wrapper.highlighted = shouldHighlight
        if (shouldHighlight) {
            wrapper.imageView.setColorFilter(wrapper.imageView.context.getColor(R.color.highlight))
        } else {
            wrapper.imageView.setColorFilter(wrapper.imageView.context.getColor(R.color.no_tint))
        }
    }

    fun highlightCard(imageView: AppCompatImageView, highlight: Boolean) {
        if (highlight) {
            imageView.setColorFilter(imageView.context.getColor(R.color.highlight))
        } else {
            imageView.setColorFilter(imageView.context.getColor(R.color.no_tint))
        }
    }

    fun dpToPixel(dp: Float, context: Context) = dp * context.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
}