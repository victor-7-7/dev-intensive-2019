package ru.skillbranch.devintensive.models.data

import java.util.*

data class ChatItem (
    val id: String,
    val avatar: String?,
    val initials: String,
    val title: String,
    val shortDescription: String?,
    /** Непрочитанные сообщения чата */
    val messageCount: Int = 0,
    val lastMessageDate: String?,
    val isOnline: Boolean = false,
    val chatType : ChatType = ChatType.SINGLE,
    var author :String? = null,
    var lastMessDate: Date = Date()
) {
}