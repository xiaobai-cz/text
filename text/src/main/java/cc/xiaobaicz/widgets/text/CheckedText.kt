package cc.xiaobaicz.widgets.text

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckedTextView

/**
 * CheckedText
 * 1. 行高适配
 * @see R.styleable.CheckedText
 * @see R.styleable.CheckedText_lineHeightX
 * @author xiaobai
 */
class CheckedText : AppCompatCheckedTextView, IText {

    private val adaptiveHelper = AdaptiveHelper()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.checkedTextViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        adaptiveHelper.handleSysAttr(this, attrs, defStyleAttr)
        adaptiveHelper.handleCustomAttr(this, attrs, R.styleable.CheckedText, defStyleAttr)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        adaptiveHelper.onMeasure(this, widthMeasureSpec, heightMeasureSpec)
    }

    override fun setMeasuredDimensionX(measuredWidth: Int, measuredHeight: Int) {
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    // 强制不使用额外高度
    override fun setFallbackLineSpacing(enabled: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return
        super.setFallbackLineSpacing(false)
    }

    override fun setLineHeight(lineHeight: Int) {
        adaptiveHelper.setLineHeight(this, lineHeight)
    }

    override fun getLineHeight(): Int {
        return adaptiveHelper.lineHeight
    }

    override val provide: TextView get() = this

}