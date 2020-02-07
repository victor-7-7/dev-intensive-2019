package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.text.TextPaint
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

class AvatarImageViewMask @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH_DP = 2F
        private const val DEF_SIZE_W_DP = 60F
        private const val DEF_SIZE_H_DP = 60F
    }

    @Px private var borderWidth = context.convertDpToPx(DEFAULT_BORDER_WIDTH_DP)
    @ColorInt private var borderColor = DEFAULT_BORDER_COLOR
    private var initials = "??"
    private lateinit var resultBm: Bitmap
    private lateinit var maskBm: Bitmap
    private lateinit var srcBm: Bitmap

    private var viewRect = Rect()
    private val maskPaint = Paint(ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageViewMask)
            borderColor = a.getColor(R.styleable.AvatarImageViewMask_aivm_borderColor,
                                            DEFAULT_BORDER_COLOR)
            // Получаем значение в пикселях
            borderWidth = a.getDimension(R.styleable.AvatarImageViewMask_aivm_borderWidth,
                                            borderWidth)
            initials = a.getString(R.styleable.AvatarImageViewMask_aivm_initials)
                                            ?: initials
            a.recycle()
        }

        scaleType = ScaleType.CENTER_CROP
        setup()
    }

    private fun setup() {
        with(maskPaint) {
            color = Color.RED
            style = Paint.Style.FILL
        }
        with(borderPaint) {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }
    }

    private fun prepareBitmaps(w: Int, h: Int) {
        maskBm = Bitmap.createBitmap(w, h, Bitmap.Config.ALPHA_8)
        resultBm = maskBm.copy(Bitmap.Config.ARGB_8888, true)
        srcBm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        /** Создаем холст с битмапом. Рисуем на холсте.
           Результат рисования отражается в битмапе! */
        Canvas(maskBm).drawOval(viewRect.toRectF(), maskPaint)
        // Покрываем результирующий битмап масковым битмапом
        Canvas(resultBm).drawBitmap(maskBm, null, viewRect, null)
        // Покрываем результирующий битмап картиночным битмапом в режиме SRC_IN
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        Canvas(resultBm).drawBitmap(srcBm, null, viewRect, maskPaint)
    }

    fun assignInitials(initials: String?) {
        if (initials.isNullOrBlank()) this.initials = "??"
        else this.initials = initials
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        Log.d("M_AvatarImageViewMask", "onDraw: ")
        canvas.drawBitmap(resultBm, viewRect, viewRect, null)
        // Чуть ужмем вьюшный прямоугольник, чтобы кольцо вписалось в границы вьюхи
        val shift = (borderWidth / 2).toInt()
        viewRect.inset(shift, shift)
        canvas.drawOval(viewRect.toRectF(), borderPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("M_AvatarImageViewMask", "onAttachedToWindow: ")
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        Log.d("M_AvatarImageViewMask", """
            onMeasure 
            W- ${MeasureSpec.toString(widthSpec)} 
            H- ${MeasureSpec.toString(heightSpec)}""".trimIndent())
        val initSize = resolveDefaultSize(widthSpec to heightSpec)
        // Вьюха должна быть равносторонней
        val size = min(initSize.first, initSize.second)
        setMeasuredDimension(size, size)
        Log.d("M_AvatarImageViewMask", "onMeasure after: " +
                "$measuredWidth x $measuredHeight")
    }

    private fun resolveDefaultSize(spec: Pair<Int, Int>): Pair<Int, Int> {
        return when(MeasureSpec.getMode(spec.first) to MeasureSpec.getMode(spec.second)) {
            MeasureSpec.UNSPECIFIED to MeasureSpec.UNSPECIFIED ->
                context.convertDpToPx(DEF_SIZE_W_DP).toInt() to
                context.convertDpToPx(DEF_SIZE_W_DP).toInt()
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
        Log.d("M_AvatarImageViewMask", "onSizeChanged: ")
        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareBitmaps(w, h)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d("M_AvatarImageViewMask", "onLayout: ")
    }

}