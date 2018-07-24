package com.finalhints.swipeablecarddeck

import android.animation.Animator
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator

class SwipeListener1(private var card: View, callback: SwipeCallback, rotation: Float = DEF_ROTATION_DEGREE) : View.OnTouchListener {


    companion object {
        private var DEF_ROTATION_DEGREE = 15f
    }

    private var mRotationDegree = rotation
    private var initialX: Float = 0f
    private var initialY: Float = 0f

    private var mActivePointerId: Int = 0
    private var srcX: Float = 0f
    private var srcY: Float = 0f

    private var parent: ViewGroup? = null
    private var parentWidth: Float = 0.toFloat()
    private var parentHeight: Float = 0.toFloat()
    private var paddingLeft: Int = 0

    private var callback: SwipeCallback? = callback

    init {
        this.initialX = card.x
        this.initialY = card.y
        this.parent = card.parent as ViewGroup
        this.parentWidth = parent!!.width.toFloat()
        this.parentHeight = parent!!.height.toFloat()
        this.paddingLeft = (card.parent as ViewGroup).paddingLeft
    }


    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {

        // handle action press event
            MotionEvent.ACTION_DOWN -> {

                //cancel running animations if any
                v.clearAnimation()

                mActivePointerId = event.getPointerId(0)

                srcX = event.x
                srcY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = event.findPointerIndex(mActivePointerId)
                if (pointerIndex < 0 || pointerIndex > 0) {
                    return true
                }

                //calculate distance moved
                val dx = event.getX(pointerIndex) - srcX
                val dy = event.getY(pointerIndex) - srcY

                val dstPosX = card.x + dx
                val dstPosY = card.y + dy

                card.x = dstPosX
                card.y = dstPosY


                val distanceObjectX = dstPosX - initialX
                val rotation = mRotationDegree * 2f * distanceObjectX / parentWidth
                card.rotation = rotation
            }

            MotionEvent.ACTION_UP -> {
                //check to see if card has moved beyond the boundaries or reset
                checkForSwipeEvent()
            }

            else -> return false
        }//if(click) return false;
        return true
    }


    /**
     *  check for swipe event make respective swipe transition
     */
    private fun checkForSwipeEvent() {

        if (isBeyondLeftPart()) {
            animateOffScreenLeft(160) { callback?.onCardExited() }
            callback?.leftExit()
        } else if (isBeyondRightPart()) {
            animateOffScreenRight(160) { callback?.onCardExited() }
            callback?.rightExit()
        } else if (isBeyondTopPart()) {
            animateOffScreenTop(160) { callback?.onCardExited() }
            callback?.topExit()
        } else if (isBeyondBottomPart()) {
            /*
            animateOffScreenBottom(160)
                    .setListener(object : Animator.AnimatorListener {

                        override fun onAnimationStart(animation: Animator) {

                        }

                        override fun onAnimationEnd(animation: Animator) {
                            callback!!.onCardExited()
                        }

                        override fun onAnimationCancel(animation: Animator) {

                        }

                        override fun onAnimationRepeat(animation: Animator) {

                        }
                    })

*/
            animateOffScreenBottom(160) { callback?.onCardExited() }
            callback?.bottomExit()
        } else {
            resetCardPosition()
        }
    }


    /* ###################  Threshold validation ###############*/

    //check if card center is beyond left part
    private fun isBeyondLeftPart(): Boolean {
        return card.x + card.width / 2 < leftBorder()
    }

    //check if card center is beyond right part
    private fun isBeyondRightPart(): Boolean {
        return card.x + card.width / 2 > rightBorder()
    }

    //check if card center is beyond top part
    private fun isBeyondTopPart(): Boolean {
        return card.y + card.height / 2 < topBorder()
    }

    //check if card center is beyond bottom part
    private fun isBeyondBottomPart(): Boolean {
        return card.y + card.height / 2 > bottomBorder()
    }


    /* ###################   Animation specific  ###############*/

    private fun resetCardPosition() {
        card.animate()
                .setDuration(200)
                .setInterpolator(OvershootInterpolator(1.5f))
                .x(initialX)
                .y(initialY)
                .rotation(0f)
    }

    private fun animateOffScreenLeft(duration: Int, callCallback: () -> Unit) {
        card.animate()
                .setDuration(duration.toLong())
                .x(-parentWidth)
                .y(0f)
                .rotation(-30f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        callCallback()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }
                })
    }

    private fun animateOffScreenRight(duration: Int, callCallback: () -> Unit) {
        card.animate()
                .setDuration(duration.toLong())
                .x(parentWidth * 2)
                .y(0f)
                .rotation(30f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        callCallback()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }
                })
    }

    private fun animateOffScreenTop(duration: Int, callCallback: () -> Unit) {
        card.animate()
                .setDuration(duration.toLong())
                .x(0f)
                .y(-parentHeight)
                .rotation(30f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        callCallback()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }
                })
    }

    private fun animateOffScreenBottom(duration: Int, callCallback: () -> Unit) {
        card.animate()
                .setDuration(duration.toLong())
                .x(0f)
                .y(parentHeight * 2)
                .rotation(30f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(p0: Animator?) {
                    }

                    override fun onAnimationEnd(p0: Animator?) {
                        callCallback()
                    }

                    override fun onAnimationCancel(p0: Animator?) {
                    }

                    override fun onAnimationStart(p0: Animator?) {
                    }
                })
    }


    /* ###################  Threshold borders ###############*/

    // threshold left border
    private fun leftBorder(): Int {
        return (parentWidth / 4f).toInt()
    }

    // threshold right border
    private fun rightBorder(): Int {
        return (3 * parentWidth / 4f).toInt()
    }

    // threshold bottom border
    private fun bottomBorder(): Int {
        return (3 * parentHeight / 4f).toInt()
    }

    // threshold top border
    private fun topBorder(): Int {
        return (parentHeight / 4f).toInt()
    }


    interface SwipeCallback {
        fun leftExit()
        fun rightExit()
        fun onCardExited()
        fun bottomExit()
        fun topExit()
    }


}
