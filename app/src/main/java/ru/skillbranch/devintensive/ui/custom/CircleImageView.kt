package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.Dimension
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.convertDpToPx
import ru.skillbranch.devintensive.extensions.convertPxToDp

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH_DP = 2F
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    // Ширина границы в пикселах
    private var borderWidth = context.convertDpToPx(DEFAULT_BORDER_WIDTH_DP)
    private var borderWidthDp = DEFAULT_BORDER_WIDTH_DP.toInt()

    private var circlePaint: Paint
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
        circlePaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = Color.GRAY
            style = Paint.Style.FILL
        }
        ringPaint = Paint(ANTI_ALIAS_FLAG).apply {
            color = borderColor
            strokeWidth = borderWidth
            style = Paint.Style.STROKE
        }
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

    fun setBorderColor(hex: String) {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
        canvas.drawOval(rectF, circlePaint)
        val delta = borderWidth * .5F
        // Радиус граничного кольца надо уменьшить, чтобы вписать его в круг
        rectF.inset(delta, delta)
        canvas.drawArc(rectF, 0F, 360F, false, ringPaint)
    }
}