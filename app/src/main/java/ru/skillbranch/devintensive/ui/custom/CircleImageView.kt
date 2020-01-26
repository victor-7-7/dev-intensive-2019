package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.convertDpToPx
import ru.skillbranch.devintensive.extensions.convertPxToDp
import ru.skillbranch.devintensive.extensions.convertSpToPx
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
    private var textPaint: Paint
//    private var avatarkaD: Drawable? = null
//    private var avatarkaB: Bitmap
//    private lateinit var scaledAva: Bitmap

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
        textPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = context.convertSpToPx(FONT_SIZE)
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }

//        avatarkaD = ResourcesCompat.getDrawable(
//                                resources, R.drawable.avatarka, null)
//        avatarkaB = BitmapFactory.decodeResource(resources, R.drawable.avatarka)

        outlineProvider = Outliner()
        clipToOutline = true
    }

    fun assignInitials(initials: String) {
        this.initials = initials
        // Если в лейауте не назначена картинка через android:src=...
        if (drawable == null) {
            // Задаем дефолтный фон для картинки
            setBackgroundColor(ContextCompat.getColor(context, R.color.color_accent))
            // Отрисовываем
            invalidate()
        } else setBackgroundColor(Color.TRANSPARENT)
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

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Масштабируем картинку
//        scaledAva = Bitmap.createScaledBitmap(avatarkaB, w, h, true)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Если в лейауте не назначена картинка через android:src=...
        if (drawable == null) {
            val baselineY = (height - textPaint.fontMetrics.top - textPaint.fontMetrics.bottom) * .5F
            // Х-координата начала текста
            val textX = (width - textPaint.measureText(initials)) * .5F
            // Рисуем на уже заданном цветном фоне инициалы юзера
            canvas.drawText(initials, textX, baselineY, textPaint)
        }
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