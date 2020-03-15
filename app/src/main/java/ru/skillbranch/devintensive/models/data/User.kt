package ru.skillbranch.devintensive.models.data

import ru.skillbranch.devintensive.extensions.humanizeDiff
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class User(
    val id: String,
    var firstName: String?,
    var lastName: String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = Date(),
    val isOnline: Boolean = false
) {

    constructor(id: String, firstName: String?, lastName: String?)
            : this(
        id = id, firstName = firstName,
        lastName = lastName, avatar = null
    )

    constructor(id: String) : this(id, "John", "Doe")

    fun toUserItem(): UserItem {

        val lastActivity = when {
            isOnline -> "online"
            lastVisit == null -> "Еще ни разу не заходил"
            else -> "Последний раз был ${lastVisit.humanizeDiff()}"
        }
        return UserItem(
            id,
            "${firstName.orEmpty()} ${lastName.orEmpty()}".trim(),
            Utils.toInitials(firstName, lastName),
            avatar,
            lastActivity,
            isOnline = isOnline
        )
    }
}