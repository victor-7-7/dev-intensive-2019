package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R

class AvatarImageViewOld @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): CircleImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val FONT_ASPECT_RATIO = .44F
    }

    private var initials: String = ""
    private var textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    init {
        setBorderWidth(0)
    }

    fun assignInitials(initials: String?) {
        if (initials.isNullOrBlank()) this.initials = "??"
        else this.initials = initials
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (drawable != null) return
        canvas.drawColor(ContextCompat.getColor(
                            context, R.color.color_avatar_single_bg))
        textPaint.textSize = height * FONT_ASPECT_RATIO
        // Вычисляем позицию текста по вертикали
        val baselineY = (height - textPaint.fontMetrics.top
                - textPaint.fontMetrics.bottom) * .5F
        // Х-координата начала текста
        val textX = (width - textPaint.measureText(initials)) * .5F
        // Рисуем на канве инициалы юзера
        canvas.drawText(initials, textX, baselineY, textPaint)
    }


}