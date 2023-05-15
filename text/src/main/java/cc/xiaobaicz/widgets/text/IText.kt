package cc.xiaobaicz.widgets.text

import android.content.Context
import android.util.TypedValue
import android.widget.TextView

/**
 * 扩展TextView
 */
interface IText {

    fun getContext(): Context

    /**
     * 设置行高
     */
    fun setLineHeight(lineHeight: Int)

    /**
     * 获取行高
     */
    fun getLineHeight(): Int

    /**
     * 是否使用额外高度
     */
    fun setFallbackLineSpacing(enabled: Boolean)

    /**
     * 设置View真实宽高
     */
    fun setMeasuredDimensionX(measuredWidth: Int, measuredHeight: Int)

    /**
     * 设置字体大小并自适应于行高
     */
    fun setTextSizeX(size: Float, lineHeight: Int = getLineHeight()) {
        setTextSizeX(TypedValue.COMPLEX_UNIT_SP, size, lineHeight)
    }

    /**
     * 设置字体大小并自适应于行高
     */
    fun setTextSizeX(size: Int, lineHeight: Int = getLineHeight()) {
        setTextSizeX(TypedValue.COMPLEX_UNIT_PX, size.toFloat(), lineHeight)
    }

    /**
     * 设置字体大小并自适应于行高
     */
    fun setTextSizeX(unit: Int, size: Float, lineHeight: Int = getLineHeight()) {
        if (lineHeight != getLineHeight())
            setLineHeight(lineHeight)
        provide.setTextSize(unit, size)
        setLineHeight(lineHeight)
    }

    val provide: TextView

}