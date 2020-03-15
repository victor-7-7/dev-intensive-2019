package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.convertDpToPx
import kotlin.math.min

class AvatarImageViewShader @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH_DP = 2F
        private const val DEF_SIZE_W_DP = 60F
        private const val DEF_SIZE_H_DP = 60F
    }

    @Px
    private var borderWidth = context.convertDpToPx(DEFAULT_BORDER_WIDTH_DP)
    @ColorInt
    private var borderColor = DEFAULT_BORDER_COLOR
    private var initials = "??"
    private lateinit var srcBm: Bitmap

    private var viewRect = Rect()
    private val avatarPaint = Paint(ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(ANTI_ALIAS_FLAG)

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageViewShader)
            borderColor = a.getColor(
                R.styleable.AvatarImageViewShader_aivs_borderColor,
                DEFAULT_BORDER_COLOR
            )
            // Получаем значение в пикселях
            borderWidth = a.getDimension(
                R.styleable.AvatarImageViewShader_aivs_borderWidth,
                borderWidth
            )
            initials = a.getString(R.styleable.AvatarImageViewShader_aivs_initials)
                ?: initials
            a.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    private fun setup() {
        with(borderPaint) {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }
    }

    private fun prepareShader(w: Int, h: Int) {
        srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(
            srcBm, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("M_AvatarImageViewShader", "onDraw: ")
        canvas.drawOval(viewRect.toRectF(), avatarPaint)
        // Чуть ужмем вьюшный прямоугольник, чтобы кольцо вписалось в границы вьюхи
        val shift = (borderWidth / 2).toInt()
        viewRect.inset(shift, shift)
        canvas.drawOval(viewRect.toRectF(), borderPaint)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        Log.d(
            "M_AvatarImageViewShader", """
            onMeasure 
            W- ${MeasureSpec.toString(widthSpec)} 
            H- ${MeasureSpec.toString(heightSpec)}""".trimIndent()
        )
        val initSize = resolveDefaultSize(widthSpec to heightSpec)
        // Вьюха должна быть равносторонней
        val size = min(initSize.first, initSize.second)
        setMeasuredDimension(size, size)
        Log.d(
            "M_AvatarImageViewShader", "onMeasure after: " +
                    "$measuredWidth x $measuredHeight"
        )
    }

    private fun resolveDefaultSize(spec: Pair<Int, Int>): Pair<Int, Int> {
        return when (MeasureSpec.getMode(spec.first) to MeasureSpec.getMode(spec.second)) {
            MeasureSpec.UNSPECIFIED to MeasureSpec.UNSPECIFIED ->
                context.convertDpToPx(DEF_SIZE_W_DP).toInt() to
                        context.convertDpToPx(DEF_SIZE_H_DP).toInt()
            MeasureSpec.UNSPECIFIED to MeasureSpec.EXACTLY,
            MeasureSpec.UNSPECIFIED to MeasureSpec.AT_MOST ->
                context.convertDpToPx(DEF_SIZE_W_DP).toInt() to
                        MeasureSpec.getSize(spec.second)
            MeasureSpec.EXACTLY to MeasureSpec.UNSPECIFIED,
            MeasureSpec.AT_MOST to MeasureSpec.UNSPECIFIED ->
                MeasureSpec.getSize(spec.first) to
                        context.convertDpToPx(DEF_SIZE_H_DP).toInt()
            else -> MeasureSpec.getSize(spec.first) to MeasureSpec.getSize(spec.second)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d("M_AvatarImageViewShader", "onSizeChanged: ")
        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareShader(w, h)
    }
}