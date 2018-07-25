package com.finalhints.swipeablecarddeck.sample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.finalhints.swipeablecarddeck.OnCardSwipeListener
import com.finalhints.swipeablecarddeck.SwipeableCardDeck
import com.finalhints.swipeablecarddeck.sample.R
import com.finalhints.swipeablecarddeck.sample.adapter.CardsAdapter
import com.finalhints.swipeablecarddeck.sample.data.repository.CardItemLocalRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val swipeCardView: SwipeableCardDeck by lazy { this.findViewById<SwipeableCardDeck>(R.id.swipeCardView) }
    private val tvBtnReset: MaterialButton by lazy { this.findViewById<MaterialButton>(R.id.tvBtnReset) }
    private val fbLeft: FloatingActionButton by lazy { this.findViewById<FloatingActionButton>(R.id.fbLeft) }
    private val fbRight: FloatingActionButton by lazy { this.findViewById<FloatingActionButton>(R.id.fbRight) }
    private val fbTop: FloatingActionButton by lazy { this.findViewById<FloatingActionButton>(R.id.fbTop) }
    private val fbBottom: FloatingActionButton by lazy { this.findViewById<FloatingActionButton>(R.id.fbBottom) }

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

        setCLickListener()
    }

    /**
     * set click listeners
     */
    private fun setCLickListener() {
        tvBtnReset.setOnClickListener { swipeCardView.reset() }

        fbLeft.setOnClickListener { swipeCardView.makeSwipeLeft() }
        fbRight.setOnClickListener { swipeCardView.makeSwipeRight() }
        fbTop.setOnClickListener { swipeCardView.makeSwipeTop() }
        fbBottom.setOnClickListener { swipeCardView.makeSwipeBottom() }
    }

}
