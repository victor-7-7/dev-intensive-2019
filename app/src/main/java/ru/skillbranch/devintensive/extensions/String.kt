package ru.skillbranch.devintensive.extensions

const val MAX_CHARS = 16

fun String.truncate(maxChars: Int = MAX_CHARS): String {
    val inStr = this.trim()
    if (inStr.length <= maxChars) return inStr
    val outString = inStr.dropLast(inStr.length - maxChars)
    return "${outString.trimEnd()}..."
}