package io.github.xiaobaicz.text

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView

/**
 * Text
 * 1. 行高适配
 * @see R.styleable.Text
 * @see R.styleable.Text_lineHeightX
 * @author xiaobai
 */
@SuppressLint("AppCompatCustomView")
class Text : TextView {

    private val textAdaptive = TextAdaptive()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        textAdaptive.handleSysAttr(this, attrs, defStyleAttr)
        textAdaptive.handleCustomAttr(this, attrs, R.styleable.Text, defStyleAttr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        textAdaptive.onMeasure(this, widthMeasureSpec, heightMeasureSpec) { w, h ->
            setMeasuredDimension(w, h)
        }
    }

    override fun setLineHeight(lineHeight: Int) {
        textAdaptive.setLineHeight(this, lineHeight.toFloat())
    }

    override fun getLineHeight(): Int {
        return textAdaptive.lineHeight.toInt()
    }

    override fun setTextSize(unit: Int, size: Float) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textAdaptive.handleTextSize(this, unit, size))
    }

}