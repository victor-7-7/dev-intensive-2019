package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.repositories.ChatRepository

class MainViewModel : ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) { chats ->
        return@map chats.groupBy { it.isArchived }
            .flatMap { (isArchived, chats) ->
                if (isArchived) listOf(Chat.archivedToChatItem(chats))
                else chats.map { it.toChatItem() }
            }
            .sortedBy { it.id.toInt() }
    }

    fun getChatData(): LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()
        val filterF = {
            val queryStr = query.value!!
            val filteredChats = chats.value!!
            result.value = if (queryStr.isEmpty()) filteredChats
            else filteredChats.filter { it.title.contains(queryStr, true) }
        }
        result.addSource(chats) { filterF.invoke() }
        result.addSource(query) { filterF.invoke() }
        return result
    }

    fun handleSearchQuery(text: String?) {
        query.value = text
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }
}