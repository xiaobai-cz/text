package cc.xiaobaicz.widgets.text

import android.graphics.Paint
import android.text.StaticLayout
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.core.widget.TextViewCompat
import kotlin.math.max

class AdaptiveHelper {

    private companion object {
        private const val MIN_LINE_HEIGHT: Int = 8
    }

    @StyleableRes
    private var attrIndex = 0

    var lineHeight: Int = MIN_LINE_HEIGHT

    /**
     * 处理自定义属性
     */
    fun handleCustomAttr(view: IText, attrs: AttributeSet?, attr: IntArray, defStyleAttr: Int) {
        val typedArray = view.getContext().obtainStyledAttributes(attrs, attr, defStyleAttr, 0)
        try {
            attrIndex = 0
            view.setLineHeight(typedArray.getDimension(attrIndex, view.provide.textSize).toInt())
        } finally {
            typedArray.recycle()
            attrIndex = 0
        }
    }

    /**
     * 处理系统属性
     */
    fun handleSysAttr(view: IText, attrs: AttributeSet?, defStyleAttr: Int) {
        val typedArray = view.getContext().obtainStyledAttributes(attrs, intArrayOf(
            android.R.attr.gravity,
            android.R.attr.includeFontPadding,
        ), defStyleAttr, 0)
        try {
            val than = view.provide
            attrIndex = 0
            // 默认垂直居中+正向
            than.gravity = typedArray.getInt(attrIndex++, Gravity.START or Gravity.CENTER_VERTICAL)
            than.includeFontPadding = typedArray.getBoolean(attrIndex++, false)
            view.setFallbackLineSpacing(false)
        } finally {
            typedArray.recycle()
            attrIndex = 0
        }
    }

    /**
     * 从新计算高度
     */
    fun onMeasure(view: IText, widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY) {
            // 重新计算高度，适应多行文本
            val than = view.provide
            val text = than.text?.toString() ?: ""
            val layout = StaticLayout.Builder.obtain(text, 0, text.length, than.paint, than.measuredWidth).build()
            val height = lineHeight * than.calculateLines(layout.lineCount) + than.paddingTop + than.paddingBottom
            view.setMeasuredDimensionX(than.measuredWidth, height)
        }
    }

    // 计算行数，受MinLines，MaxLines影响
    private fun TextView.calculateLines(realLines: Int): Int = when {
        realLines < minLines -> minLines
        realLines > maxLines -> maxLines
        else -> realLines
    }

    fun setLineHeight(view: TextView, lineHeight: Int) {
        this.lineHeight = max(lineHeight, MIN_LINE_HEIGHT)
        adaptiveTextSize(view)
        TextViewCompat.setLineHeight(view, this.lineHeight)
        view.requestLayout()
        view.invalidate()
    }

    private fun adaptiveTextSize(view: TextView) {
        val paint = view.paint
        var metrics = paint.fontMetrics
        while (metrics.fontHeight(view) > lineHeight) {
            paint.textSize -= max((metrics.fontHeight(view) - lineHeight) / 2, 1f)
            metrics = paint.fontMetrics
        }
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, paint.textSize)
    }

    private fun Paint.FontMetrics.fontHeight(view: TextView): Float = if (view.includeFontPadding)
        bottom - top
    else
        descent - ascent

}