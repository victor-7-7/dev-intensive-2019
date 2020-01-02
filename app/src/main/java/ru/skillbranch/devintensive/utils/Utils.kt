package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?): Pair<String?, String?> {
        if (fullName.isNullOrBlank()) return "null" to "null"
        val parts:List<String>? = fullName.split(" ")
        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)
        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
        TODO("not implemented")
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var initials = ""
        if (!firstName.isNullOrBlank() && firstName.trim()[0].isLetter())
            initials = firstName.trim()[0].toUpperCase().toString()
        if (!lastName.isNullOrBlank() && lastName.trim()[0].isLetter())
            initials += lastName.trim()[0].toUpperCase().toString()
        if (initials == "") return "null" else return initials
    }
}















