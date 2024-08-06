package hu.ocist.enaplo.login.ui.message

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.responses.MessageResponse
import hu.ocist.enaplo.login.databinding.MessageListRowBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.stringToDateFormat

class TeacherMessageRecyclerViewAdapter(val activity: HomeActivity) :
    RecyclerView.Adapter<TeacherMessageRecyclerViewAdapter.MessageViewHolder>() {
    private val items = mutableListOf<MessageResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        this.parent = parent
        return MessageViewHolder(MessageListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.date.text = stringToDateFormat(menuItem.date)
        holder.binding.from.text = menuItem.teacher
        holder.binding.text.text = menuItem.message
        if (menuItem.seen) {
            holder.binding.seen.setImageResource(R.drawable.ic_baseline_mark_email_read_24)
            holder.binding.date.setTypeface(null, Typeface.NORMAL)
            holder.binding.from.setTypeface(null, Typeface.NORMAL)
            holder.binding.text.setTypeface(null, Typeface.NORMAL)
        } else {
            holder.binding.seen.setImageResource(R.drawable.ic_baseline_markunread_24)
            holder.binding.date.setTypeface(null, Typeface.BOLD)
            holder.binding.from.setTypeface(null, Typeface.BOLD)
            holder.binding.text.setTypeface(null, Typeface.BOLD)
        }

        holder.binding.root.setOnClickListener {
            activity.navigateToMessage(menuItem.id)
            activity.setTitle(holder.binding.text.context.getString(R.string.message))
        }
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