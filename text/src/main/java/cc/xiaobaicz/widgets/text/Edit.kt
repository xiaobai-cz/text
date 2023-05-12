package cc.xiaobaicz.widgets.text

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding

/**
 * Edit
 * 1. 行高适配
 * @see R.styleable.Edit
 * @see R.styleable.Edit_lineHeightX
 * @author xiaobai
 */
class Edit : AppCompatEditText, IText {

    private val adaptiveHelper = AdaptiveHelper()

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        adaptiveHelper.handleSysAttr(this, attrs, defStyleAttr)
        adaptiveHelper.handleCustomAttr(this, attrs, R.styleable.Edit, defStyleAttr)
        setPadding(0)
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