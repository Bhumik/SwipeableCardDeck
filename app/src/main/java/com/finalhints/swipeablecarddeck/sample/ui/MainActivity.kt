package com.finalhints.swipeablecarddeck.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.finalhints.swipeablecarddeck.OnCardSwipeListener
import com.finalhints.swipeablecarddeck.SwipeableCardDeck
import com.finalhints.swipeablecarddeck.sample.R
import com.finalhints.swipeablecarddeck.sample.adapter.CardsAdapter
import com.finalhints.swipeablecarddeck.sample.data.repository.CardItemLocalRepository
import com.finalhints.swipeablecarddeck.sample.datamodel.CardItem
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private val swipeCardView: SwipeableCardDeck by lazy { this.findViewById<SwipeableCardDeck>(R.id.swipeCardView) }
    private val tvBtnReset: MaterialButton by lazy { this.findViewById<MaterialButton>(R.id.tvBtnReset) }

    private val repository by lazy { CardItemLocalRepository() }
    private lateinit var mAdapter: CardsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mViewModel = ViewModelProvider(this, CardItemViewModel.Factory(repository)).get(CardItemViewModel::class.java)

        mAdapter = CardsAdapter(this, mViewModel.getCardItems())

        swipeCardView.setAdapter(mAdapter)

        swipeCardView.setSwipeListener(object : OnCardSwipeListener {
            override fun onSwipedLeft(dataObject: Any) {

            }

            override fun onSwipedRight(dataObject: Any) {

            }

            override fun onSwipedTop(dataObject: Any) {

            }

            override fun onSwipedBottom(dataObject: Any) {

            }

        })

        tvBtnReset.setOnClickListener {
            swipeCardView.reset()
            mAdapter.add(CardItem("sfsd"))
            mAdapter.notifyDataSetChanged()
        }
    }

}
