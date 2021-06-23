package com.moonlightbutterfly.makao

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class CardImageProvider(private val context: Context) {

    private val providersMap = mapOf(
        Suit.DIAMONDS to DiamondsProvider(),
        Suit.CLUBS to ClubsProvider(),
        Suit.HEARTS to HeartsProvider(),
        Suit.SPADES to SpadesProvider(),
    )

    fun provideCardImage(card: Card): Drawable? = providersMap[card.suit]?.provideCard(card.rank)

    fun provideCardBackImage(): Drawable? = ContextCompat.getDrawable(context, R.drawable.blue_back)

    fun provideCardImageRotated(card: Card): Drawable = providersMap.getValue(card.suit).provideCard(card.rank).rotate()

    private fun Drawable.rotate(): Drawable {
        val arrayDrawable = arrayOf(this)
        return object : LayerDrawable(arrayDrawable) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.rotate(180f, (this@rotate.bounds.width() / 2).toFloat(),
                    (this@rotate.bounds.height() / 2).toFloat()
                )
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    private inner class DiamondsProvider : Provider {

        override fun provideCard(value: Rank): Drawable {
            @DrawableRes val drawableId = when (value) {
                Rank.TWO -> R.drawable.two_diamonds
                Rank.THREE -> R.drawable.three_diamonds
                Rank.FOUR -> R.drawable.four_diamonds
                Rank.FIVE -> R.drawable.five_diamonds
                Rank.SIX -> R.drawable.six_diamonds
                Rank.SEVEN -> R.drawable.seven_diamonds
                Rank.EIGHT -> R.drawable.eight_diamonds
                Rank.NINE -> R.drawable.nine_diamonds
                Rank.TEN -> R.drawable.ten_diamonds
                Rank.JACK -> R.drawable.jack_diamonds
                Rank.QUEEN -> R.drawable.queen_diamonds
                Rank.KING -> R.drawable.king_diamonds
                Rank.ACE -> R.drawable.ace_diamonds
            }
            return ContextCompat.getDrawable(context, drawableId)!!
        }
    }

    private inner class ClubsProvider : Provider {

        override fun provideCard(value: Rank): Drawable {
            @DrawableRes val drawableId = when (value) {
                Rank.TWO -> R.drawable.two_clubs
                Rank.THREE -> R.drawable.three_clubs
                Rank.FOUR -> R.drawable.four_clubs
                Rank.FIVE -> R.drawable.five_clubs
                Rank.SIX -> R.drawable.six_clubs
                Rank.SEVEN -> R.drawable.seven_clubs
                Rank.EIGHT -> R.drawable.eight_clubs
                Rank.NINE -> R.drawable.nine_clubs
                Rank.TEN -> R.drawable.ten_clubs
                Rank.JACK -> R.drawable.jack_clubs
                Rank.QUEEN -> R.drawable.queen_clubs
                Rank.KING -> R.drawable.king_clubs
                Rank.ACE -> R.drawable.ace_clubs
            }
            return ContextCompat.getDrawable(context, drawableId)!!
        }
    }

    private inner class SpadesProvider : Provider {

        override fun provideCard(value: Rank): Drawable {
            @DrawableRes val drawableId = when (value) {
                Rank.TWO -> R.drawable.two_spades
                Rank.THREE -> R.drawable.three_spades
                Rank.FOUR -> R.drawable.four_spades
                Rank.FIVE -> R.drawable.five_spades
                Rank.SIX -> R.drawable.six_spades
                Rank.SEVEN -> R.drawable.seven_spades
                Rank.EIGHT -> R.drawable.eight_spades
                Rank.NINE -> R.drawable.nine_spades
                Rank.TEN -> R.drawable.ten_spades
                Rank.JACK -> R.drawable.jack_spades
                Rank.QUEEN -> R.drawable.queen_spades
                Rank.KING -> R.drawable.king_spades
                Rank.ACE -> R.drawable.ace_spades
            }
            return ContextCompat.getDrawable(context, drawableId)!!
        }
    }

    private inner class HeartsProvider : Provider {

        override fun provideCard(value: Rank): Drawable {
            @DrawableRes val drawableId = when (value) {
                Rank.TWO -> R.drawable.two_hearts
                Rank.THREE -> R.drawable.three_hearts
                Rank.FOUR -> R.drawable.four_hearts
                Rank.FIVE -> R.drawable.five_hearts
                Rank.SIX -> R.drawable.six_hearts
                Rank.SEVEN -> R.drawable.seven_hearts
                Rank.EIGHT -> R.drawable.eight_hearts
                Rank.NINE -> R.drawable.nine_hearts
                Rank.TEN -> R.drawable.ten_hearts
                Rank.JACK -> R.drawable.jack_hearts
                Rank.QUEEN -> R.drawable.queen_hearts
                Rank.KING -> R.drawable.king_hearts
                Rank.ACE -> R.drawable.ace_hearts
            }
            return ContextCompat.getDrawable(context, drawableId)!!
        }
    }

    interface Provider {
        fun provideCard(value: Rank): Drawable
    }

}