package com.finalhints.swipeablecarddeck

interface SwipeableCardDeckPropertyEvent {

    fun allowRightSwipe(): Boolean

    fun allowLeftSwipe(): Boolean

    fun allowBottomSwipe(): Boolean

    fun allowTopSwipe(): Boolean

}
