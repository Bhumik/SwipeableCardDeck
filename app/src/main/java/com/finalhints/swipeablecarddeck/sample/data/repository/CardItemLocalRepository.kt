package com.finalhints.swipeablecarddeck.sample.data.repository

import com.finalhints.swipeablecarddeck.sample.R
import com.finalhints.swipeablecarddeck.sample.datamodel.CardItem
import java.util.*

class CardItemLocalRepository : CardItemRepository {

    override fun getCardItems(): ArrayList<CardItem> {
        val cardItems = ArrayList<CardItem>()
        cardItems.add(CardItem("card1", R.drawable.dummyimg1))
        cardItems.add(CardItem("card2", R.drawable.dummyimg2))
        cardItems.add(CardItem("card3", R.drawable.dummyimg3))
        cardItems.add(CardItem("card4", R.drawable.dummyimg4))
        cardItems.add(CardItem("card5", R.drawable.dummyimg5))
        cardItems.add(CardItem("card6", R.drawable.dummyimg6))
        cardItems.add(CardItem("card7", R.drawable.dummyimg7))
        return cardItems

    }
}
