package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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

    private var ringPaint: Paint
//    private var avatarkaD: Drawable? = null
    private var avatarkaB: Bitmap
    private lateinit var scaledAva: Bitmap

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
//        avatarkaD = ResourcesCompat.getDrawable(
//                                resources, R.drawable.avatarka, null)
        avatarkaB = BitmapFactory.decodeResource(resources, R.drawable.avatarka)

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

// Custom Drawing - https://developer.android.com/training/custom-views/custom-drawing

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Масштабируем картинку
        scaledAva = Bitmap.createScaledBitmap(avatarkaB, w, h, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())

        if (avatarkaB != null) {
            // Круглая область
            val path = Path().apply { addOval(rectF, Path.Direction.CW) }
            canvas.save()
            // Ограничиваем отрисовку этой областью
            canvas.clipPath(path)
            // Рисуем картинку в круге
            canvas.drawBitmap(scaledAva, 0f, 0f, null)
            // Снимаем ограничение
            canvas.restore()
        } else {
            // Рисуем шаблонную круглую заготовку
        }
        val delta = borderWidth * .5F
        // Радиус граничного кольца надо уменьшить, чтобы вписать его в круг
        rectF.inset(delta, delta)
        canvas.drawArc(rectF, 0F, 360F, false, ringPaint)
    }
}