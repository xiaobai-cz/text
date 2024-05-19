package io.github.xiaobaicz.text

import android.annotation.SuppressLint
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.StyleableRes
import androidx.core.content.res.use
import androidx.core.widget.TextViewCompat
import kotlin.math.max
import kotlin.math.roundToInt

class TextAdaptive {

    var lineHeight: Float = 0f

    private var textSize: Float = 0f

    /**
     * 处理系统属性
     */
    @SuppressLint("ResourceType")
    fun handleSysAttr(view: TextView, attrs: AttributeSet?, defStyleAttr: Int) {
        @StyleableRes val indexGravity = 0
        @StyleableRes val indexIncludeFontPadding = 1
        @StyleableRes val indexTextSize = 2
        view.context.obtainStyledAttributes(attrs, intArrayOf(android.R.attr.gravity, android.R.attr.includeFontPadding, android.R.attr.textSize), defStyleAttr, 0).use { a ->
            // 默认垂直居中+正向
            view.gravity = a.getInt(indexGravity, Gravity.START or Gravity.CENTER_VERTICAL)
            view.includeFontPadding = a.getBoolean(indexIncludeFontPadding, true)
            textSize = a.getDimension(indexTextSize, view.textSize)
        }
    }

    /**
     * 处理自定义属性
     */
    fun handleCustomAttr(view: TextView, attrs: AttributeSet?, attr: IntArray, defStyleAttr: Int) {
        @StyleableRes val indexLineHeight = 0
        view.context.obtainStyledAttributes(attrs, attr, defStyleAttr, 0).use { a ->
            lineHeight = a.getDimension(indexLineHeight, textSize * 1.5f)
        }
        setLineHeight(view, lineHeight)
    }

    /**
     * 从新计算高度
     */
    fun onMeasure(view: TextView, widthMeasureSpec: Int, heightMeasureSpec: Int, callback: (Int, Int) -> Unit) {
        if (View.MeasureSpec.getMode(heightMeasureSpec) != View.MeasureSpec.EXACTLY) {
            // 重新计算高度，适应多行文本
            val text = view.text?.toString() ?: ""
            val layout = StaticLayout.Builder.obtain(text, 0, text.length, view.paint, calculateTextAreaWidth(view)).build()
            val height = calculateHeight(view, layout)
            callback.invoke(view.measuredWidth, height)
        }
    }

    // 计算文本区域的实际宽度
    private fun calculateTextAreaWidth(view: TextView): Int {
        var width = view.measuredWidth - view.paddingStart - view.paddingEnd
        val drawables = view.compoundDrawablesRelative
        val drawableStart = drawables[0]
        val drawableEnd = drawables[2]
        drawableStart?.apply {
            width -= bounds.width() + view.compoundDrawablePadding
        }
        drawableEnd?.apply {
            width -= bounds.width() + view.compoundDrawablePadding
        }
        return width
    }

    // 计算文本区域的实际宽度
    private fun calculateTextAreaHeight(view: TextView, layout: Layout): Int {
        return (lineHeight * view.calculateLines(layout.lineCount)).roundToInt()
    }

    // 计算文本区域的实际宽度
    private fun calculateHeight(view: TextView, layout: Layout): Int {
        var height = calculateTextAreaHeight(view, layout) + view.paddingTop + view.paddingBottom
        val drawables = view.compoundDrawablesRelative
        val drawableTop = drawables[1]
        val drawableBottom = drawables[3]
        drawableTop?.apply {
            height += bounds.width() + view.compoundDrawablePadding
        }
        drawableBottom?.apply {
            height += bounds.width() + view.compoundDrawablePadding
        }
        return height
    }

    // 计算行数，受MinLines，MaxLines影响
    private fun TextView.calculateLines(realLines: Int): Int = when {
        realLines < minLines -> minLines
        realLines > maxLines -> maxLines
        else -> realLines
    }

    fun setLineHeight(view: TextView, lineHeight: Float) {
        this.lineHeight = max(lineHeight, 0f)
        TextViewCompat.setLineHeight(view, this.lineHeight.toInt())
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    fun handleTextSize(view: TextView, unit: Int, size: Float): Float {
        val textSize = TypedValue.applyDimension(unit, size, view.resources.displayMetrics)
        val maxTextSize = maxTextSize(view.includeFontPadding)
        this.textSize = textSize
        return if (textSize > maxTextSize) maxTextSize else textSize
    }

    private fun maxTextSize(includeFontPadding: Boolean): Float {
        return with(TextPaint()) {
            textSize = 16f
            val height = fontHeight(includeFontPadding)
            textSize / height * lineHeight
        }
    }

    private fun Paint.fontHeight(includeFontPadding: Boolean): Float = with(fontMetrics) {
        if (includeFontPadding)
            bottom - top
        else
            descent - ascent
    }

}