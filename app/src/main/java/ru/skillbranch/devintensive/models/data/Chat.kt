package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*

data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int {
        return messages.map { !it.isReaded }.size
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date {
//        var date = messages.first().date
//        messages.forEach { if (it.date > date) date = it.date }
//        return date
        return messages.lastOrNull()!!.date
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> =
        when(val msg = messages.lastOrNull()) {
            is TextMessage -> (msg.text ?: "") to msg.from.firstName
            is ImageMessage -> "${msg.from.firstName} отправил фото" to
                                            msg.from.firstName
            else -> "???" to "!!!"
    }

    private fun isSingle(): Boolean = members.size == 1

    fun toChatItem(): ChatItem {
        return if (isSingle()) {
            val user = members.first()
            ChatItem(
                id,
                user.avatar,
                Utils.toInitials(user.firstName, user.lastName) ?: "??",
                "${user.firstName ?: "Fn"} ${user.lastName ?: ""}".trim(),
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate().shortFormat(),
                user.isOnline,
                if (isArchived) ChatType.ARCHIVE else ChatType.SINGLE,
                lastMessDate = lastMessageDate()
            )
        } else {
            ChatItem(
                id,
                null,
                "",
                title,
                lastMessageShort().first,
                unreadableMessageCount(),
                lastMessageDate().shortFormat(),
                false,
                if (isArchived) ChatType.ARCHIVE else ChatType.GROUP,
                lastMessageShort().second,
                lastMessageDate()
            )
        }
    }
}

enum class ChatType {
    SINGLE,
    GROUP,
    ARCHIVE
}

