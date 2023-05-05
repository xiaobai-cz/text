package cc.xiaobaicz.widgets.text

import android.content.Context
import android.graphics.Paint.FontMetrics
import android.os.Build
import android.text.StaticLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import androidx.annotation.StyleableRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.TextViewCompat
import kotlin.math.max

/**
 * Text
 * 1. 行高适配
 * @see R.styleable.Text
 * @see R.styleable.Text_lineHeightX
 * @author xiaobai
 */
class Text : AppCompatTextView {

    @StyleableRes
    private var attrIndex = 0

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.textViewStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        handleSysAttr(context, attrs, defStyleAttr)
        handleCustomAttr(context, attrs, defStyleAttr)
    }

    // 处理自定义属性
    private fun handleCustomAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Text, defStyleAttr, 0)
        try {
            lineHeight = typedArray.getDimension(R.styleable.Text_lineHeightX, textSize).toInt()
        } finally {
            typedArray.recycle()
        }
    }

    // 处理系统属性
    private fun handleSysAttr(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, intArrayOf(
            android.R.attr.gravity,
            android.R.attr.includeFontPadding,
        ), defStyleAttr, 0)
        try {
            attrIndex = 0
            // 默认垂直居中+正向
            gravity = typedArray.getInt(attrIndex++, Gravity.START or Gravity.CENTER_VERTICAL)
            includeFontPadding = typedArray.getBoolean(attrIndex++, false)
            isFallbackLineSpacing = false
        } finally {
            typedArray.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
            // 重新计算高度，适应多行文本
            val layout = StaticLayout.Builder.obtain(text, 0, text.length, paint, measuredWidth).build()
            val height = lineHeightX * calculateLines(layout.lineCount) + paddingTop + paddingBottom
            setMeasuredDimension(measuredWidth, height)
        }
    }

    // 计算行数，受MinLines，MaxLines影响
    private fun calculateLines(realLines: Int): Int = when {
        realLines < minLines -> minLines
        realLines > maxLines -> maxLines
        else -> realLines
    }

    // 强制不使用额外高度
    override fun setFallbackLineSpacing(enabled: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) return
        super.setFallbackLineSpacing(false)
    }

    var lineHeightX: Int = MIN_LINE_HEIGHT
        private set

    fun changeTextSize() {
        lineHeight = lineHeightX
    }

    override fun setLineHeight(lineHeight: Int) {
        lineHeightX = max(lineHeight, MIN_LINE_HEIGHT)
        adaptiveTextSize()
        TextViewCompat.setLineHeight(this, lineHeightX)
        requestLayout()
        invalidate()
    }

    private fun adaptiveTextSize() {
        var metrics = paint.fontMetrics
        while (metrics.fontHeight > lineHeightX) {
            paint.textSize -= max((metrics.fontHeight - lineHeightX) / 2, 1f)
            metrics = paint.fontMetrics
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX, paint.textSize)
    }

    private val FontMetrics.fontHeight: Float get() = if (includeFontPadding)
        bottom - top
    else
        descent - ascent

    private companion object {
        private const val MIN_LINE_HEIGHT: Int = 8
    }

}