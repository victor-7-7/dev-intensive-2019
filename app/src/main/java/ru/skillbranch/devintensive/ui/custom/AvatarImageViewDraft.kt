package ru.skillbranch.devintensive.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.Px
import androidx.core.animation.doOnRepeat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.toRectF
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.convertDpToPx
import kotlin.math.max
import kotlin.math.min
import kotlin.math.truncate

class AvatarImageViewDraft @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH_DP = 2F
        private const val DEF_SIZE_W_DP = 40F
        private const val DEF_SIZE_H_DP = 40F
        private const val FONT_ASPECT_RATIO = .4F
        val bgColors = arrayOf(
            "#7BC862",
            "#E17076",
            "#FAA774",
            "#6EC9CB",
            "#65AADD",
            "#A695E7",
            "#EE7AAE",
            "#2196F3"
        )
    }
    private var borderWidthDp = DEFAULT_BORDER_WIDTH_DP.toInt()
    @Px private var borderWidth = context.convertDpToPx(DEFAULT_BORDER_WIDTH_DP)
    @ColorInt private var borderColor = DEFAULT_BORDER_COLOR
    private var initials = "??"
    private var initialMode = false
    private var animSize = 0

    private var viewRect = Rect()
    private var borderRect = Rect()
    private val avatarPaint = Paint(ANTI_ALIAS_FLAG)
    private val borderPaint = Paint(ANTI_ALIAS_FLAG)
    private val textPaint = Paint(ANTI_ALIAS_FLAG)

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView)
            borderColor = a.getColor(R.styleable.AvatarImageView_aiv_borderColor,
                                            DEFAULT_BORDER_COLOR)
            // Получаем значение в пикселях
            borderWidth = a.getDimension(R.styleable.AvatarImageView_aiv_borderWidth,
                                            borderWidth)
            initials = a.getString(R.styleable.AvatarImageView_aiv_initials)
                                            ?: initials
            a.recycle()
        }
        scaleType = ScaleType.CENTER_CROP
        setup()
        setOnLongClickListener { handleLongClick() }
    }

    private fun setup() {
        with(borderPaint) {
            color = borderColor
            style = Paint.Style.STROKE
            strokeWidth = borderWidth
        }
        textPaint.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    }

    private fun prepareShader(w: Int, h: Int) {
        if (w == 0 || drawable == null) return
        val bm = drawable.toBitmap(w, h, Bitmap.Config.ARGB_8888)
        avatarPaint.shader = BitmapShader(bm, Shader.TileMode.CLAMP,
                                                Shader.TileMode.CLAMP)
    }

    override fun onDraw(canvas: Canvas) {
        if (drawable == null || initialMode) drawInitials(canvas)
        else drawAvatar(canvas)
        if (borderWidth > 0)
            // Рисуем кольцо по границе вюхи (вьюха у нас круглая)
            canvas.drawOval(borderRect.toRectF(), borderPaint)
    }

    private fun drawAvatar(c: Canvas) {
        c.drawOval(viewRect.toRectF(), avatarPaint)
    }

    private fun drawInitials(c: Canvas) {
        textPaint.color = initialsToColor()
        c.drawOval(viewRect.toRectF(), textPaint)
        with(textPaint) {
            color = borderColor
            textAlign = Paint.Align.CENTER
            textSize = height * FONT_ASPECT_RATIO
        }
        val shiftY = (textPaint.descent() + textPaint.ascent()) * .5F
        c.drawText(initials, viewRect.exactCenterX(),
                    viewRect.exactCenterY() - shiftY, textPaint)
    }

    private fun initialsToColor(): Int {
        val b = initials[0].toByte()
        val len = bgColors.size
        val d = b / len.toDouble()
        val index = ((d - truncate(d)) * len).toInt()
        return Color.parseColor((bgColors[index]))
    }

    private fun handleLongClick(): Boolean {
        val va = ValueAnimator.ofInt(width, (width * 1.33).toInt()).apply {
            duration = 700
            interpolator = LinearInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 1
        }
        va.addUpdateListener {
            Log.d("M_AvatarImageView", "ValueAnimator update: " +
                    "animSize=$animSize animatedValue=${it.animatedValue}")
            animSize = it.animatedValue as Int
            requestLayout()
        }
        va.doOnRepeat { toggleMode() }
        va.start()
        return true
    }

    private fun toggleMode() {
        initialMode = !initialMode
        invalidate()
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        Log.d("M_AvatarImageView", """
            onMeasure 
            W- ${MeasureSpec.toString(widthSpec)} 
            H- ${MeasureSpec.toString(heightSpec)}""".trimIndent())
        val initSize = resolveDefaultSize(widthSpec to heightSpec)
        Log.d("M_AvatarImageView", "onMeasure after resolveDefaultSize: " +
                "${initSize.first} x ${initSize.second}")
        // Вьюха должна быть равносторонней
        val size = min(initSize.first, initSize.second)
        // Добавим возможность анимировать размер вьюхи по лонгклику
        val aSize = max(animSize, size)
        Log.d("M_AvatarImageView", "onMeasure: size=$size " +
                "animSize=$animSize aSize=$aSize")
        setMeasuredDimension(aSize, aSize)
        Log.d("M_AvatarImageView", "onMeasure after setMeasuredDimension: " +
                "$measuredWidth x $measuredHeight")
    }

    private fun resolveDefaultSize(spec: Pair<Int, Int>): Pair<Int, Int> {
        return when(MeasureSpec.getMode(spec.first) to MeasureSpec.getMode(spec.second)) {
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
        if (w == 0) return
        with(viewRect) {
            left = 0
            top = 0
            right = w
            bottom = h
        }
        prepareShader(w, h)
        correctBorderRectSize()
    }

    private fun correctBorderRectSize() {
        borderRect.set(viewRect)
        // Чуть ужмем прямоугольник кольца, чтобы кольцо
        // вписалось в границы вьюшного прямоугольника
        val shift = (borderWidth / 2).toInt()
        borderRect.inset(shift, shift)
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        if (!initialMode) prepareShader(width, height)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        if (!initialMode) prepareShader(width, height)
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        if (!initialMode) prepareShader(width, height)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.borderWidthDp = borderWidthDp
        savedState.borderWidth  = borderWidth
        savedState.borderColor = borderColor
        savedState.initialMode  = initialMode
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        /** Чтобы вьюха восстановила свое предыдущее состояние,
         * она должна иметь заданный id в лейауте */
        if (state is SavedState) {
            borderWidthDp = state.borderWidthDp
            borderWidth = state.borderWidth
            borderColor = state.borderColor
            initialMode = state.initialMode
            borderPaint.strokeWidth = borderWidth
            borderPaint.color = borderColor
            textPaint.color = borderColor
        }
    }

    fun assignInitials(initials: String?) {
        if (initials.isNullOrBlank()) this.initials = "??"
        else this.initials = initials
        if (initialMode) invalidate()
    }

    @Dimension
    fun getBorderWidth(): Int = borderWidthDp

    fun setBorderWidth(@Dimension widthDp: Int) {
        if (widthDp < 0 || widthDp == borderWidthDp) return
        borderWidthDp = widthDp
        borderWidth = context.convertDpToPx(widthDp.toFloat())
        borderPaint.strokeWidth = borderWidth
        // Скорректируем по размеру прямоугольник кольца
        correctBorderRectSize()
        invalidate()
    }

    fun getBorderColor():Int = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
        borderPaint.color = borderColor
        textPaint.color = borderColor
        invalidate()
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = ContextCompat.getColor(context, colorId)
        borderPaint.color = borderColor
        textPaint.color = borderColor
        invalidate()
    }

    private class SavedState: BaseSavedState, Parcelable {
        var borderWidthDp: Int = 0
        var borderWidth = 0F
        var borderColor: Int = 0
        var initialMode = true

        constructor(superState: Parcelable?): super(superState)

        constructor(src: Parcel) : super(src) {
            // Restore state from parcel
            borderWidthDp = src.readInt()
            borderWidth = src.readFloat()
            borderColor = src.readInt()
            initialMode = src.readInt() == 1
        }

        override fun writeToParcel(dst: Parcel, flags: Int) {
            super.writeToParcel(dst, flags)
            dst.writeInt(borderWidthDp)
            dst.writeFloat(borderWidth)
            dst.writeInt(borderColor)
            dst.writeInt(if (initialMode) 1 else 0)
        }

        override fun describeContents() = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(pcl: Parcel) = SavedState(pcl)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }
}