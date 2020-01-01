package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR


fun Date.format(pattern:String="HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value:Int, units:TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when(units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(date: Date = Date()): String {
    val diff = this.time - date.time
    val m = diff / MINUTE
    val h = diff / HOUR
    val d = diff / DAY

    return when(diff) {
        in -SECOND..SECOND -> "только что"
        in -45 * SECOND until -SECOND -> "несколько секунд назад"
        in 1 + SECOND..45 * SECOND -> "через несколько секунд"
        in -75 * SECOND until -45 * SECOND -> "минуту назад"
        in 1 + 45 * SECOND..75 * SECOND -> "через минуту"
        in -45 * MINUTE until -75 * SECOND -> "${-m} ${flexTime(m.toInt())} назад"
        in 1 + 75 * SECOND..45 * MINUTE -> "через $m ${flexTime(m.toInt())}"
        in -75 * MINUTE until -45 * MINUTE -> "час назад"
        in 1 + 45 * MINUTE..75 * MINUTE -> "через час"
        in -22 * HOUR until -75 * MINUTE -> "${-h} " +
                            "${flexTime(h.toInt(), TimeUnits.HOUR)} назад"
        in 1 + 75 * MINUTE..22 * HOUR -> "через $h " +
                                    flexTime(h.toInt(), TimeUnits.HOUR)
        in -26 * HOUR until -22 * HOUR -> "день назад"
        in 1 + 22 * HOUR..26 * HOUR -> "через день"
        in -360 * DAY until -26 * HOUR -> "${-d} " +
                            "${flexTime(d.toInt(), TimeUnits.DAY)} назад"
        in 1 + 26 * HOUR..360 * DAY -> "через $d " +
                                    flexTime(d.toInt(), TimeUnits.DAY)
        else -> if (diff < 0) "более года назад" else "более чем через год"
    }
}

val firstSet = listOf(-9,-8,-7,-6,-5,0,5,6,7,8,9)
val secondSet = listOf(-4,-3,-2,2,3,4)
/**
* {1,21,31,41...} - минуту час день
* {2,22,32,42...
* 3,23,33,43...
* 4,24,34,44...} - минуты часа дня
* {5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20...} - минут часов дней
*/
private fun flexTime(num:Int, unit:TimeUnits = TimeUnits.MINUTE): String {
    return when {
        // Число либо в 11..14, либо оканчивается не на {1,2,3,4}
        (num in 11..14 || num % 10 in firstSet) && unit == TimeUnits.MINUTE -> "минут"
        (num in 11..14 || num % 10 in firstSet) && unit == TimeUnits.HOUR -> "часов"
        (num in 11..14 || num % 10 in firstSet) && unit == TimeUnits.DAY -> "дней"
        // Число оканчивается на {2,3,4} и не из 11..14
        num % 10 in secondSet && unit == TimeUnits.MINUTE -> "минуты"
        num % 10 in secondSet && unit == TimeUnits.HOUR -> "часа"
        num % 10 in secondSet && unit == TimeUnits.DAY -> "дня"
        // Число оканчивается на 1 и не 11
        unit == TimeUnits.MINUTE -> "минуту"
        unit == TimeUnits.HOUR -> "час"
        unit == TimeUnits.DAY -> "день"
        // Недостижимый else, но компилятор требует
        else -> "intervals"
    }
}

enum class TimeUnits {SECOND, MINUTE, HOUR, DAY}