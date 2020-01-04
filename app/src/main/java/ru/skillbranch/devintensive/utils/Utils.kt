package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName:String?): Pair<String?, String?> {
        val parts = fullName?.trim()?.split(" ")
        val firstName = parts?.getOrNull(0)?.ifEmpty { null }
        val lastName = parts?.getOrNull(1)?.ifEmpty { null }
        return firstName to lastName
    }

    private val trans = mapOf('а' to "a", 'б' to "b", 'в' to "v", 'г' to "g",
                        'д' to "d", 'е' to "e", 'ё' to "e", 'ж' to "zh", 'з' to "z",
                        'и' to "i", 'й' to "i", 'к' to "k", 'л' to "l", 'м' to "m",
                        'н' to "n", 'о' to "o", 'п' to "p", 'р' to "r", 'с' to "s",
                        'т' to "t", 'у' to "u", 'ф' to "f", 'х' to "h", 'ц' to "c",
                        'ч' to "ch", 'ш' to "sh", 'щ' to "sh'", 'ъ' to "", 'ы' to "i",
                        'ь' to "", 'э' to "e", 'ю' to "yu", 'я' to "ya",
                        //----------------------------------------------
                        'А' to "A", 'Б' to "B", 'В' to "V", 'Г' to "G",
                        'Д' to "D", 'Е' to "E", 'Ё' to "E", 'Ж' to "Zh", 'З' to "Z",
                        'И' to "I", 'Й' to "I", 'К' to "K", 'Л' to "L", 'М' to "M",
                        'Н' to "N", 'О' to "O", 'П' to "P", 'Р' to "R", 'С' to "S",
                        'Т' to "T", 'У' to "U", 'Ф' to "F", 'Х' to "H", 'Ц' to "C",
                        'Ч' to "Ch", 'Ш' to "Sh", 'Щ' to "Sh'", 'Ъ' to "", 'Ы' to "I",
                        'Ь' to "", 'Э' to "E", 'Ю' to "Yu", 'Я' to "Ya")

    fun transliteration(payload: String, divider: String = " "): String {
        var outStr = ""
        for (ch in payload.trim().toCharArray()) {
            when {
                trans.containsKey(ch) -> outStr += trans[ch]
                ch == ' ' -> outStr += divider
                else -> outStr += ch
            }
        }
        return outStr
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val fni = firstName.orEmpty().trim().getOrNull(0)
                        ?.toUpperCase()?.toString() ?: ""
        val lni = lastName.orEmpty().trim().getOrNull(0)
                        ?.toUpperCase()?.toString() ?: ""
        return "$fni$lni".ifEmpty { null }
    }
}















