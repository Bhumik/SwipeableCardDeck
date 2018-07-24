package com.finalhints.swipeablecarddeck.sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.finalhints.swipeablecarddeck.sample.data.repository.CardItemRepository
import com.finalhints.swipeablecarddeck.sample.datamodel.CardItem
import java.util.*


class CardItemViewModel internal constructor(internal var mCardItemRepository: CardItemRepository) : ViewModel() {

    /**
     * get card item to showcase
     */
    fun getCardItems(): ArrayList<CardItem> {
        return mCardItemRepository.cardItems
    }

    class Factory(private val pRepository: CardItemRepository) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: CardItemRepository = pRepository

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return CardItemViewModel(mRepository) as T
        }
    }


}
