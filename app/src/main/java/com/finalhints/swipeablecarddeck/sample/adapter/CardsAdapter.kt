package com.finalhints.swipeablecarddeck.sample.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.finalhints.swipeablecarddeck.sample.R
import com.finalhints.swipeablecarddeck.sample.datamodel.CardItem
import java.util.*

/**
 * card item adapter
 */
class CardsAdapter(context: Context, private val cards: ArrayList<CardItem>) : ArrayAdapter<CardItem>(context, -1) {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val (name, imageId) = getItem(position)
        val view = layoutInflater.inflate(R.layout.adapter_item_card, parent, false)


        (view.findViewById<View>(R.id.card_image) as ImageView).setImageResource(imageId)
        (view.findViewById<View>(R.id.helloText) as TextView).text = name

        return view
    }

    override fun getItem(position: Int): CardItem {
        return cards[position]
    }

    override fun getCount(): Int {
        return cards.size
    }
}
