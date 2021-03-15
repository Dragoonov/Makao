package com.moonlightbutterfly.makao

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    val cardViewsMap = mutableMapOf<Card, ImageView>()

    private var dataSet: MutableList<Card> = mutableListOf()
    private lateinit var imageProvider: CardImageProvider

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: ImageView = view.findViewById(R.id.card)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.card_item, viewGroup, false)
        imageProvider = CardImageProvider(viewGroup.context)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val card = dataSet[position]
        viewHolder.card.setImageDrawable(imageProvider.provideCardImage(card))
        viewHolder.card.setOnClickListener {
            it as ImageView
            if (card.highlighted) {
                it.setColorFilter(Color.argb(0, 0, 0, 0))
                cardViewsMap.remove(card)
            } else {
                cardViewsMap[card] = it
                it.setColorFilter(Color.argb(100, 255, 255, 0))
            }
            card.highlighted = !card.highlighted
        }
    }

    override fun getItemCount() = dataSet.size

    fun addCard(card: Card) {
        dataSet.add(card)
        notifyItemInserted(dataSet.size-1)
    }

    fun removeCard(card: Card) {
        dataSet.remove(card)
    }
}