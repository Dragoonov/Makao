package com.moonlightbutterfly.makao

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

class CardImageProvider(private val context: Context) {

    private val providersMap = mapOf(
            CardType.DIAMONDS to DiamondsProvider(),
            CardType.CLUBS to ClubsProvider(),
            CardType.HEARTS to HeartsProvider(),
            CardType.SPADES to SpadesProvider(),
    )

    fun provideCardImage(card: Card): Drawable? = providersMap[card.type]?.provideCard(card.value)

    private inner class DiamondsProvider : Provider {

        override fun provideCard(value: CardValue): Drawable? {
            @DrawableRes val drawableId = when (value) {
                CardValue.TWO -> R.drawable.two_diamonds
                CardValue.THREE -> R.drawable.three_diamonds
                CardValue.FOUR -> R.drawable.four_diamonds
                CardValue.FIVE -> R.drawable.five_diamonds
                CardValue.SIX -> R.drawable.six_diamonds
                CardValue.SEVEN -> R.drawable.seven_diamonds
                CardValue.EIGHT -> R.drawable.eight_diamonds
                CardValue.NINE -> R.drawable.nine_diamonds
                CardValue.TEN -> R.drawable.ten_diamonds
                CardValue.JACK -> R.drawable.jack_diamonds
                CardValue.QUEEN -> R.drawable.queen_diamonds
                CardValue.KING -> R.drawable.king_diamonds
                CardValue.ACE -> R.drawable.ace_diamonds
            }
            return ContextCompat.getDrawable(context, drawableId)
        }
    }

    private inner class ClubsProvider : Provider {

        override fun provideCard(value: CardValue): Drawable? {
            @DrawableRes val drawableId = when (value) {
                CardValue.TWO -> R.drawable.two_clubs
                CardValue.THREE -> R.drawable.three_clubs
                CardValue.FOUR -> R.drawable.four_clubs
                CardValue.FIVE -> R.drawable.five_clubs
                CardValue.SIX -> R.drawable.six_clubs
                CardValue.SEVEN -> R.drawable.seven_clubs
                CardValue.EIGHT -> R.drawable.eight_clubs
                CardValue.NINE -> R.drawable.nine_clubs
                CardValue.TEN -> R.drawable.ten_clubs
                CardValue.JACK -> R.drawable.jack_clubs
                CardValue.QUEEN -> R.drawable.queen_clubs
                CardValue.KING -> R.drawable.king_clubs
                CardValue.ACE -> R.drawable.ace_clubs
            }
            return ContextCompat.getDrawable(context, drawableId)
        }
    }

    private inner class SpadesProvider : Provider {

        override fun provideCard(value: CardValue): Drawable? {
            @DrawableRes val drawableId = when (value) {
                CardValue.TWO -> R.drawable.two_spades
                CardValue.THREE -> R.drawable.three_spades
                CardValue.FOUR -> R.drawable.four_spades
                CardValue.FIVE -> R.drawable.five_spades
                CardValue.SIX -> R.drawable.six_spades
                CardValue.SEVEN -> R.drawable.seven_spades
                CardValue.EIGHT -> R.drawable.eight_spades
                CardValue.NINE -> R.drawable.nine_spades
                CardValue.TEN -> R.drawable.ten_spades
                CardValue.JACK -> R.drawable.jack_spades
                CardValue.QUEEN -> R.drawable.queen_spades
                CardValue.KING -> R.drawable.king_spades
                CardValue.ACE -> R.drawable.ace_spades
            }
            return ContextCompat.getDrawable(context, drawableId)
        }
    }

    private inner class HeartsProvider : Provider {

        override fun provideCard(value: CardValue): Drawable? {
            @DrawableRes val drawableId = when (value) {
                CardValue.TWO -> R.drawable.two_hearts
                CardValue.THREE -> R.drawable.three_hearts
                CardValue.FOUR -> R.drawable.four_hearts
                CardValue.FIVE -> R.drawable.five_hearts
                CardValue.SIX -> R.drawable.six_hearts
                CardValue.SEVEN -> R.drawable.seven_hearts
                CardValue.EIGHT -> R.drawable.eight_hearts
                CardValue.NINE -> R.drawable.nine_hearts
                CardValue.TEN -> R.drawable.ten_hearts
                CardValue.JACK -> R.drawable.jack_hearts
                CardValue.QUEEN -> R.drawable.queen_hearts
                CardValue.KING -> R.drawable.king_hearts
                CardValue.ACE -> R.drawable.ace_hearts
            }
            return ContextCompat.getDrawable(context, drawableId)
        }
    }

    interface Provider {
        fun provideCard(value: CardValue): Drawable?
    }

}