package com.finalhints.swipeablecarddeck.sample.datamodel

import com.finalhints.swipeablecarddeck.sample.R

/**
 * data model to store card item details
 */
data class CardItem(
        var name: String = "",
        var imageId: Int = R.drawable.dummyimg1
) {
}
