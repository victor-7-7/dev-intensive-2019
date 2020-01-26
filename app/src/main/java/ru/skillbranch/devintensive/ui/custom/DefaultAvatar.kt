package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.PictureDrawable
import android.text.TextPaint
import androidx.core.content.ContextCompat
import ru.skillbranch.devintensive.R

class DefaultAvatar @JvmOverloads constructor (val context: Context,
               var pic: Picture = Picture()) : PictureDrawable(pic) {

    companion object {
        private const val W = 200
        private const val H = 200
        private const val FONT_ASPECT_RATIO = .44F
    }
    private var textPaint: Paint

    init {
        textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = H * FONT_ASPECT_RATIO
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
        }
    }

    fun assignInitials(initials: String) {
        val canvas = pic.beginRecording(W, H)
        // Закрашиваем канву
        canvas.drawColor(ContextCompat.getColor(context, R.color.color_accent))
        // Вычисляем позицию текста по вертикали
        val baselineY = (H - textPaint.fontMetrics.top
                - textPaint.fontMetrics.bottom) * .5F
        // Х-координата начала текста
        val textX = (W - textPaint.measureText(initials)) * .5F
        // Рисуем на канве инициалы юзера
        canvas.drawText(initials, textX, baselineY, textPaint)
        pic.endRecording()
    }
}