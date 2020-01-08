package ru.skillbranch.devintensive.extensions

import android.content.Context
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt
import androidx.appcompat.app.AppCompatActivity as Activity



fun Activity.hideKeyboard(view: View) {
    val imm: InputMethodManager = this.getSystemService(
                        Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}

// Любая активити в своем корне имеет ViewGroup с id == android.R.id.content
fun Activity.getRootView(): View = findViewById<View>(android.R.id.content)

fun Context.convertDpToPx(dp: Float): Float = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics)

fun Activity.isKeyboardOpen(): Boolean {
    val frame = Rect()
    // Получаем размеры фрейма, охватывающего ту часть View, которая видима
    // юзеру, то есть ничем не перекрыта (в том числе - клавиатурой)
    getRootView().getWindowVisibleDisplayFrame(frame)
    // Разность между исходной высотой активити и высотой ее видимой
    // области даст нам высоту вьюхи, перекрывшей активити.
    /** Но только если !!! для активити не задан в манифесте атрибут
     * android:windowSoftInputMode=”adjustResize" */
    val overlayHeight = getRootView().height - frame.height()
    // Учтем, что перекрыть часть активити (или сместить ее вверх) может
    // не только клавиатура, но если высота перекрытия (смещения) больше
    // некоторого порога (напр 70dp), то это скорее всего клавиатура
    return overlayHeight > convertDpToPx(70F).roundToInt()
}

