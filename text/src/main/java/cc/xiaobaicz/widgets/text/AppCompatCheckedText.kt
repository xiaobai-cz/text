package cc.xiaobaicz.widgets.text

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatCheckedTextView

/**
 * CheckedText
 * 1. 行高适配
 * @see R.styleable.CheckedText
 * @see R.styleable.CheckedText_lineHeightX
 * @author xiaobai
 */
class AppCompatCheckedText : AppCompatCheckedTextView {

    private val textAdaptive = TextAdaptive()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.checkedTextViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        textAdaptive.handleSysAttr(this, attrs, defStyleAttr)
        textAdaptive.handleCustomAttr(this, attrs, R.styleable.CheckedText, defStyleAttr)
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