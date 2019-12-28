package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val date: Date = Date(),
    val isIncoming: Boolean = false
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId = -1   
        fun makeMessage(from: User?, chat: Chat, date: Date = Date(),
                        type: String = "text", payload: Any?): BaseMessage {
            lastId++
            return when(type) {
                "image" -> ImageMessage("$lastId", from, chat, date,
                                            image = payload as String)
                else -> TextMessage("$lastId", from, chat, date,
                                            text = payload as String)
            }
        }
    }
}