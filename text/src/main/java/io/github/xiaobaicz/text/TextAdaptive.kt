package io.github.xiaobaicz.text

import android.annotation.SuppressLint
import android.graphics.Paint
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
            view.includeFontPadding = a.getBoolean(indexIncludeFontPadding, false)
            textSize = a.getDimension(indexTextSize, view.textSize)
        }
    }

    /**
     * 处理自定义属性
     */
    fun handleCustomAttr(view: TextView, attrs: AttributeSet?, attr: IntArray, defStyleAttr: Int) {
        @StyleableRes val indexLineHeight = 0
        view.context.obtainStyledAttributes(attrs, attr, defStyleAttr, 0).use { a ->
            lineHeight = a.getDimension(indexLineHeight, textSize * 1.2f)
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
            val layout = StaticLayout.Builder.obtain(text, 0, text.length, view.paint, view.measuredWidth).build()
            val height = lineHeight * view.calculateLines(layout.lineCount) + view.paddingTop + view.paddingBottom
            callback.invoke(view.measuredWidth, height.toInt())
        }
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