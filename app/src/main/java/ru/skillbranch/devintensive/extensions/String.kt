package ru.skillbranch.devintensive.extensions

const val MAX_CHARS = 16

fun String.truncate(maxChars: Int = MAX_CHARS): String {
    val inStr = this.trim()
    if (maxChars < 1 || inStr.length <= maxChars) return inStr
    val outStr = inStr.dropLast(inStr.length - maxChars)
    return "${outStr.trimEnd()}..."
}

fun String.stripHtml(): String = this.removeHtmlTags()
                                .removeHtmlSpecialChars()
                                .removeExtraSpaces()

fun String.removeHtmlTags(): String =
            this.replace(Regex("<[^<]*?>"), "")

fun String.removeHtmlSpecialChars(): String =
            this.replace(Regex("&#?\\w{2,6};"), "")

fun String.removeExtraSpaces(): String =
            this.trim().replace(Regex(" {2,}"), " ")