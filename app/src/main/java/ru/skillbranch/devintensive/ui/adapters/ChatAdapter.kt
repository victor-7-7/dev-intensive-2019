package ru.skillbranch.devintensive.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_archive.*
import kotlinx.android.synthetic.main.item_chat_group.*
import kotlinx.android.synthetic.main.item_chat_single.*
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType

class ChatAdapter(private val listener: (ChatItem) -> Unit)
            : RecyclerView.Adapter<ChatAdapter.ChatItemViewHolder>() {

    companion object {
        private const val ARCHIVE_TYPE = 0
        private const val SINGLE_TYPE = 1
        private const val GROUP_TYPE = 2
    }

    var items: List<ChatItem> = listOf()

    override fun getItemViewType(position: Int): Int =
        when (items[position].chatType) {
            ChatType.ARCHIVE -> ARCHIVE_TYPE
            ChatType.SINGLE -> SINGLE_TYPE
            ChatType.GROUP -> GROUP_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            SINGLE_TYPE -> SingleViewHolder(inflater.inflate(
                                    R.layout.item_chat_single, parent, false))
            GROUP_TYPE -> GroupViewHolder(inflater.inflate(
                                    R.layout.item_chat_group, parent, false))
            else -> SingleViewHolder(inflater.inflate(
                                    R.layout.item_chat_single, parent, false))
        }
/*        val convertView = inflater.inflate(R.layout.item_chat_single,
                                    parent, false)
        Log.d("M_ChatAdapter", "onCreateViewHolder")
        return SingleViewHolder(convertView)*/
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ChatItemViewHolder, position: Int) {
        Log.d("M_ChatAdapter", "onBindViewHolder $position")
        holder.bind(items[position], listener)
    }

    fun updateData(data: List<ChatItem>){
        Log.d("M_ChatAdapter", "update data adapter - " +
                "new data ${data.size} hash : ${data.hashCode()} " +
                "old data ${items.size} hash : ${items.hashCode()}")

        val diffCallback = object: DiffUtil.Callback() {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean
                    = items[oldPos].id == data[newPos].id

            override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean
                    = items[oldPos].hashCode() == data[newPos].hashCode()

            override fun getOldListSize(): Int = items.size

            override fun getNewListSize(): Int = data.size
        }
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        items = data
        diffResult.dispatchUpdatesTo(this)
    }

    abstract inner class ChatItemViewHolder(convertView: View)
                        : RecyclerView.ViewHolder(convertView), LayoutContainer {
        override val containerView: View?
            get() = itemView

        abstract fun bind(item: ChatItem, listener: (ChatItem) -> Unit)
    }

    inner class SingleViewHolder(convertView: View)
                        : ChatItemViewHolder(convertView),
                            ItemTouchViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            iv_avatar_single.assignInitials(item.initials)

            if (item.avatar == null) {
                Glide.with(itemView)
                    .clear(iv_avatar_single)
            } else {
                Glide.with(itemView)
                    .load(item.avatar)
                    .into(iv_avatar_single)
            }
            sv_indicator.visibility = if(item.isOnline) View.VISIBLE else View.GONE
            with(tv_date_single){
                visibility = if(item.lastMessageDate != null)
                                    View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_single){
                visibility = if(item.messageCount > 0)
                    View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_title_single.text = item.title
            tv_message_single.text = item.shortDescription
            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(
                                    App.applicationContext(), R.color.color_gray))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(ContextCompat.getColor(
                                    App.applicationContext(), R.color.color_gray_light))
        }
    }

    inner class GroupViewHolder(convertView: View)
                                : ChatItemViewHolder(convertView),
                                ItemTouchViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            iv_avatar_group.assignInitials(item.initials)

            with(tv_date_group){
                visibility = if(item.lastMessageDate != null)
                    View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_group){
                visibility = if(item.messageCount > 0)
                    View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_title_group.text = item.title
            tv_message_group.text = item.shortDescription
            tv_message_author_group.text = item.author
            tv_message_author_group.visibility = if(item.messageCount > 0)
                                    View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(
                                    App.applicationContext(), R.color.color_gray))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(ContextCompat.getColor(
                                    App.applicationContext(), R.color.color_gray_light))
        }
    }

    inner class ArchiveViewHolder(convertView: View)
                                    : ChatItemViewHolder(convertView),
                                    ItemTouchViewHolder {

        override fun bind(item: ChatItem, listener: (ChatItem) -> Unit) {

            iv_avatar_archive.assignInitials(item.initials)

            with(tv_date_archive){
                visibility = if(item.lastMessageDate != null)
                    View.VISIBLE else View.GONE
                text = item.lastMessageDate
            }
            with(tv_counter_archive){
                visibility = if(item.messageCount > 0)
                    View.VISIBLE else View.GONE
                text = item.messageCount.toString()
            }
            tv_title_archive.text = item.title
            tv_message_archive.text = item.shortDescription
            tv_message_author_archive.text = item.author
            tv_message_author_archive.visibility = if(item.messageCount > 0)
                View.VISIBLE else View.GONE
            itemView.setOnClickListener {
                listener.invoke(item)
            }
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(ContextCompat.getColor(
                                App.applicationContext(), R.color.color_gray))
        }

        override fun onItemCleared() {
            itemView.setBackgroundColor(ContextCompat.getColor(
                                App.applicationContext(), R.color.color_gray_light))
        }
    }
}