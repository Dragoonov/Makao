package com.moonlightbutterfly.makao

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class CardAdapter : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    var dataSet: MutableList<Card> = mutableListOf()
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
        viewHolder.card.setImageDrawable(imageProvider.provideCardImage(dataSet[position]))
    }

    override fun getItemCount() = dataSet.size
}