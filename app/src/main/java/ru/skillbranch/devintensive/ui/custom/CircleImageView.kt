package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.*
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH_DP = 2F
        private const val FONT_SIZE = 42F
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    // Ширина границы в пикселах
    private var borderWidth = context.convertDpToPx(DEFAULT_BORDER_WIDTH_DP)
    private var borderWidthDp = DEFAULT_BORDER_WIDTH_DP.toInt()

    private var initials: String = ""

    private var ringPaint: Paint

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(R.styleable.CircleImageView_cv_borderColor,
                                            DEFAULT_BORDER_COLOR)
            // Получаем значение в пикселях
            borderWidth = a.getDimension(R.styleable.CircleImageView_cv_borderWidth,
                                                            borderWidth)
            // Преобразуем пиксели в dp
            borderWidthDp = context.convertPxToDp(borderWidth).toInt()
            a.recycle()
        }

        ringPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = borderColor
            strokeWidth = borderWidth
            style = Paint.Style.STROKE
        }

        outlineProvider = Outliner()
        clipToOutline = true
    }

    @Dimension
    fun getBorderWidth(): Int = borderWidthDp

    fun setBorderWidth(@Dimension dp: Int) {
        borderWidthDp = dp
        borderWidth = context.convertDpToPx(dp.toFloat())
        ringPaint.strokeWidth = borderWidth
        invalidate()
    }

    fun getBorderColor():Int = borderColor

    fun setBorderColor(hex: String) { borderColor = Color.parseColor(hex) }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val delta = borderWidth * .5F
        // Радиус граничного кольца надо уменьшить, чтобы вписать его в круг
        canvas.drawArc(delta, delta, width - delta, height - delta,
                            0F, 360F, false, ringPaint)
    }

    class Outliner() : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val rect = Rect(0, 0, view!!.width, view.height)
            val radius = min(view.width, view.height)
            outline!!.setRoundRect(rect, radius.toFloat())
        }
    }
}