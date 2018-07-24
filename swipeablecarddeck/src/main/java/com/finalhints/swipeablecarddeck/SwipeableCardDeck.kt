package com.finalhints.swipeablecarddeck

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.FrameLayout

class SwipeableCardDeck @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = R.attr.SwipeStyle) : BaseAdapterView<Adapter>(context, attrs, defStyle), SwipeableCardDeckPropertyEvent {

    /**
     * Default Values
     */
    private val SCALE_XY_OFFSET = 0.045f

    private val TRANSLATION_Y_OFFSET = 90f//45;

    private var MAX_VISIBLE = 3

    private var ROTATION_DEGREES = 15f

    private var ALLOW_BOTTOM_SWIPE: Boolean = true

    private var ALLOW_TOP_SWIPE: Boolean = true

    private var ALLOW_RIGHT_SWIPE: Boolean = true

    private var ALLOW_LEFT_SWIPE: Boolean = true


    /**
     * instance of adapter to be set
     */
    private var mAdapter: Adapter? = null

    private var mAdapterCount: Int = 0
        get() = mAdapter?.count ?: 0


    /**
     * index to start rendering
     * (once any card will be removed, it will get incremented to render can be resume from next index)
     */
    private var mStartFrom = 0

    /**
     * last active view on which touch listener will be set
     */
    private var mLastActiveView = 0

    /**
     * maximum visible card to be shown in stack
     */
    private var mMaxVisible = MAX_VISIBLE


    /* if rendering is in progress */
    private var mIsRendering = false

    /* instance of current active view */
    private var mActiveCard: View? = null


    private var mDataSetObserver: AdapterDataSetObserver? = null

    /**
     * instance of internal swipe listner to manage card removal and pass event in main listner
     */
    private var cardGestureSwipeListener: SwipeListener1? = null


    /**
     * card swipe listener to pass swipe event
     */
    private var mSwipeListener: OnCardSwipeListener? = null

    /**
     * card click listener to pass card click event
     */
    private var mOnItemClickListener: OnItemClickListener? = null

    init {

        /**
         * init default value from xml attributes
         */
        val a = context.obtainStyledAttributes(attrs, R.styleable.SwipeableCardDeck, defStyle, 0)
        MAX_VISIBLE = a.getInt(R.styleable.SwipeableCardDeck_max_visible, MAX_VISIBLE)
        ROTATION_DEGREES = a.getFloat(R.styleable.SwipeableCardDeck_rotation_degrees, ROTATION_DEGREES)

        ALLOW_TOP_SWIPE = a.getBoolean(R.styleable.SwipeableCardDeck_allow_top_swipe, true)
        ALLOW_BOTTOM_SWIPE = a.getBoolean(R.styleable.SwipeableCardDeck_allow_bottom_swipe, true)
        ALLOW_LEFT_SWIPE = a.getBoolean(R.styleable.SwipeableCardDeck_allow_left_swipe, true)
        ALLOW_RIGHT_SWIPE = a.getBoolean(R.styleable.SwipeableCardDeck_allow_right_swipe, true)

        mMaxVisible = MAX_VISIBLE
        a.recycle()
    }

    override fun getSelectedView(): View? {
        return mActiveCard
    }

    override fun requestLayout() {
        if (!mIsRendering) {
            super.requestLayout()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        //remove all previous view to refresh view
        removeAllViewsInLayout()
        mAdapter?.let {
            mIsRendering = true

            //add all child card
            layoutChildren()

            //set listener on top visible card
            setTopView()

            mIsRendering = false
        }
    }

    /**
     * method to go through necessary child views and display stack card with scale and translationY to make it look like stack
     */
    private fun layoutChildren() {

        // rendering start index
        var startingIndex = mStartFrom

        // as we are not starting rendering from 0, calculate final no of card to render
        val availableCards = mAdapterCount - startingIndex
        mMaxVisible = Math.min(availableCards, mMaxVisible)


        // add initial view of card upto maxVisible (start Index + Max Card to be shown in stack)
        var viewStack = 0
        while (startingIndex < mStartFrom + mMaxVisible) {
            val newUnderChild = mAdapter!!.getView(startingIndex, null, this)
            makeAndAddView(newUnderChild, viewStack)
            startingIndex++
            viewStack++
        }

        //if we are reaching end of list and there are less card than maxVisible then recalculate last active card(top visible card)
        if (startingIndex >= mAdapterCount) {
            mLastActiveView = --viewStack
            return
        }
        //else top visible card will always be 3rd of stack
        mLastActiveView = mMaxVisible - 1
    }


    /**
     * method to add view with its calculated XY scaling and Y position
     */
    private fun makeAndAddView(child: View, pos: Int) {
        val lp = child.layoutParams as FrameLayout.LayoutParams
        child.scaleX = child.scaleX - (pos * SCALE_XY_OFFSET)
        child.scaleY = child.scaleY - (pos * SCALE_XY_OFFSET)
        child.y = child.translationY + (pos * TRANSLATION_Y_OFFSET)

        addViewInLayout(child, 0, lp, true)

        //calculate child bound and layout them
        val needToMeasure = child.isLayoutRequested
        if (needToMeasure) {
            val childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight + lp.leftMargin + lp.rightMargin, lp.width)
            val childHeightSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom + lp.topMargin + lp.bottomMargin, lp.height)
            child.measure(childWidthSpec, childHeightSpec)
        } else {
            cleanupLayoutState(child)
        }

        val w = child.measuredWidth
        val h = child.measuredHeight
        val childLeft = paddingLeft + lp.leftMargin
        val childTop = paddingTop + lp.topMargin
        child.layout(childLeft, childTop, childLeft + w, childTop + h)

    }


    /**
     * Set the top view and add the fling listener
     */
    private fun setTopView() {
        if (childCount > 0) {
            mActiveCard = getChildAt(mLastActiveView)
            mActiveCard?.let {

                cardGestureSwipeListener = SwipeListener1(it, object : SwipeListener1.SwipeCallback {
                    override fun leftExit() {

                    }

                    override fun rightExit() {

                    }

                    override fun onCardExited() {
                        mStartFrom++
                        requestLayout()
                    }

                    override fun bottomExit() {

                    }

                    override fun topExit() {

                    }
                })

                it.setOnTouchListener(cardGestureSwipeListener)
            }
        }
    }

    fun reset() {
        mStartFrom = 0
        mLastActiveView = 0
        mMaxVisible = MAX_VISIBLE
        layoutChildren()
        requestLayout()
    }

    override fun getAdapter(): Adapter? {
        return mAdapter
    }

    /**
     * set adapter
     */
    override fun setAdapter(adapter: Adapter) {
        mAdapter?.unregisterDataSetObserver(mDataSetObserver)

        mAdapter = adapter

        mDataSetObserver = AdapterDataSetObserver()
        mAdapter?.registerDataSetObserver(mDataSetObserver)
    }

    /**
     * set swipe listener
     * to receive swipe events
     */
    fun setSwipeListener(OnCardFlingListener: OnCardSwipeListener) {
        this.mSwipeListener = OnCardFlingListener
    }

    /**
     * set onCLick listener
     * to receive click events
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    override fun generateLayoutParams(attrs: AttributeSet): ViewGroup.LayoutParams {
        return FrameLayout.LayoutParams(context, attrs)
    }

    interface OnItemClickListener {
        fun onItemClicked(itemPosition: Int, dataObject: Any)
    }


    /**
     * make top card swipe to left
     */
    fun makeSwipeLeft() {
        //cardGestureSwipeListener?.selectLeft()
    }

    /**
     * make top card swipe to left
     */
    fun makeSwipeRight() {
        //cardGestureSwipeListener?.selectRight()
    }

    /**
     * make top card swipe to left
     */
    fun makeSwipeTop() {
        //cardGestureSwipeListener?.selectTop()
    }

    /**
     * make top card swipe to bottom
     */
    fun makeSwipeBottom() {
        //cardGestureSwipeListener?.selectBottom()
    }


    //date set observer
    private inner class AdapterDataSetObserver : DataSetObserver() {
        override fun onChanged() {
            Log.d("TEST", "===== AdapterDataSetObserver onCHanged: ");
            requestLayout()
        }

        override fun onInvalidated() {
            Log.d("TEST", "===== AdapterDataSetObserver onInvalidated: ");
            requestLayout()
        }
    }


    override fun allowRightSwipe(): Boolean {
        return ALLOW_RIGHT_SWIPE
    }

    override fun allowLeftSwipe(): Boolean {
        return ALLOW_LEFT_SWIPE
    }

    override fun allowBottomSwipe(): Boolean {
        return ALLOW_BOTTOM_SWIPE
    }

    override fun allowTopSwipe(): Boolean {
        return ALLOW_TOP_SWIPE
    }

}
