package hu.ocist.enaplo.login.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.MessageResponse
import hu.ocist.enaplo.login.databinding.MessageListRowBinding
import hu.ocist.enaplo.login.ui.stringToDateFormat

class MessageRecyclerViewAdapter :
    RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder>() {
    private val items = mutableListOf<MessageResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        this.parent = parent
        return MessageViewHolder(MessageListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.date.text = stringToDateFormat(menuItem.date)
        holder.binding.teacher.text = menuItem.sender
        holder.binding.text.text = menuItem.message
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<MessageResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class MessageViewHolder(
        val binding: MessageListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}