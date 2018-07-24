package com.finalhints.swipeablecarddeck

import android.content.Context
import android.util.AttributeSet
import android.widget.Adapter
import android.widget.AdapterView

abstract class BaseAdapterView<T : Adapter> : AdapterView<T> {

    var heightMeasureSpec: Int = 0
        private set
    var widthMeasureSpec: Int = 0
        private set


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun setSelection(i: Int) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        this.widthMeasureSpec = widthMeasureSpec
        this.heightMeasureSpec = heightMeasureSpec
    }

}
