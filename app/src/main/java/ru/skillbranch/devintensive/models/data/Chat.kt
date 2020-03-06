package ru.skillbranch.devintensive.models.data

import androidx.annotation.VisibleForTesting
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.TextMessage
import ru.skillbranch.devintensive.utils.Utils
import java.util.*
data class Chat(
    val id: String,
    val title: String,
    val members: List<User> = listOf(),
    /** сообщения чата НЕ упорядочены по датам */
    var messages: MutableList<BaseMessage> = mutableListOf(),
    var isArchived: Boolean = false
) {

    companion object {
        /** Метод вызывается в MainViewModel только когда в списке chats
        * есть хотя бы один архивный чат */
        // Аннотация позволит вызвать метод в Java коде как статич. метод класса
        @JvmStatic
        fun archivedToChatItem(archivedChats: List<Chat>): ChatItem {
            val msgs = archivedChats.flatMap { it.messages }
            val lastMess = if (msgs.isEmpty()) null else msgs.maxBy { it.date }

            return ChatItem(
                "-1",
                null,
                "",
                "Архив чатов",
                when (lastMess) {
                    null -> "Сообщений еще нет"
                    is TextMessage -> lastMess.text
                    else -> "${lastMess.from.firstName} - отправил фото"
                },
                archivedChats.sumBy { it.unreadableMessageCount() },
                lastMess?.date?.shortFormat(),
                false,
                ChatType.ARCHIVE,
                lastMess?.from?.firstName,
                lastMess?.date
                )
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun unreadableMessageCount(): Int = if (messages.isEmpty()) 0
                            else messages.map { !it.isReaded }.size


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageDate(): Date? {
        // Если групповой чат только что создан кнопкой fab,
        // то в нем нет сообщений
        return if (messages.isEmpty()) null
                else messages.maxBy { it.date }!!.date
    }

    /** Получаем текст последнего по дате сообщения (1) в чате
     * и имя автора (2) сообщения. Если список сообщений пуст,
     * то получаем информстроку (1) и null (2) */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun lastMessageShort(): Pair<String, String?> =
        if (messages.isEmpty()) Pair("Сообщений еще нет", null)
        else when(val msg = messages.maxBy { it.date }) {
            is TextMessage -> msg.text!! to msg.from.firstName
            else -> "${msg!!.from.firstName} - отправил фото" to
                    msg.from.firstName
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
                lastMessageDate()?.shortFormat(),
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
                lastMessageDate()?.shortFormat(),
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

