package ru.skillbranch.devintensive.models

import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        private var lastId = -1
        fun makeMessage(from: User?, chat: Chat, date: Date = Date(),
                        type: String = "text", payload: Any?,
                        isIncoming: Boolean = false): BaseMessage {
            lastId++
            return when(type) {
                "text" -> TextMessage("$lastId", from, chat,  isIncoming, date,
                                            text = payload as String)
                "image" -> ImageMessage("$lastId", from, chat, isIncoming, date,
                                            image = payload as String)
                // Вариант для переставленных местами аргументов type и payload
                else -> if (payload == "text" || payload == "image")
                     makeMessage(from, chat, date, payload as String, type, isIncoming)
                        else makeMessage(from, chat, date, payload = type)
            }
        }
    }
}