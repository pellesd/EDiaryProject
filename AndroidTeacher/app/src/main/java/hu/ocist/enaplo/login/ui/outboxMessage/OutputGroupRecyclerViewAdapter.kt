package hu.ocist.enaplo.login.ui.outboxMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.responses.MessageGroupResponse
import hu.ocist.enaplo.login.databinding.MessageGroupListRowBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.stringToDateFormat

class OutputGroupRecyclerViewAdapter(
    private val activity: HomeActivity
) :
    RecyclerView.Adapter<OutputGroupRecyclerViewAdapter.MessageGroupViewHolder>() {
    private val items = mutableListOf<MessageGroupResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageGroupViewHolder {
        this.parent = parent
        return MessageGroupViewHolder(MessageGroupListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MessageGroupViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.date.text = stringToDateFormat(menuItem.date)
        holder.binding.validUntil.text =
            "Érvényes: ${stringToDateFormat(menuItem.validUntil, "yyyy.MM.dd")}"
        holder.binding.text.text = menuItem.message

        holder.binding.root.setOnClickListener {
            activity.navigateToGroupMembers(menuItem.id)
            activity.setTitle(holder.binding.root.context.getString(R.string.sentTo))
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<MessageGroupResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class MessageGroupViewHolder(
        val binding: MessageGroupListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}