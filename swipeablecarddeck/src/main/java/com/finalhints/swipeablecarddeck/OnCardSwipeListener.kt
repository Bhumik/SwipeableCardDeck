package com.finalhints.swipeablecarddeck


interface OnCardSwipeListener {

    fun onSwipedLeft(dataObject: Any)

    fun onSwipedRight(dataObject: Any)

    fun onSwipedTop(dataObject: Any)

    fun onSwipedBottom(dataObject: Any)

}