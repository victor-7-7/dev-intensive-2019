package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.extensions.humanizeDiff
import java.util.*

class TextMessage(
    id: String,
    from: User?,
    chat: Chat,
    date: Date = Date(),
    isIncoming: Boolean = false,
    var text: String?
) : BaseMessage(id, from, chat, date, isIncoming) {
    override fun formatMessage(): String = "id:$id ${from?.firstName} " +
            "${if(isIncoming) "получил" else "отправил"} сообщение \"$text\" " +
            date.humanizeDiff()
}