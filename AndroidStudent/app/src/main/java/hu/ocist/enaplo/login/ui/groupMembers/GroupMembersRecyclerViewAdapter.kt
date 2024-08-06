package hu.ocist.enaplo.login.ui.groupMembers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.databinding.GroupMembersListRowBinding

class GroupMembersRecyclerViewAdapter :
    RecyclerView.Adapter<GroupMembersRecyclerViewAdapter.GroupMembersViewHolder>() {
    private val items = mutableListOf<String>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMembersViewHolder {
        this.parent = parent
        return GroupMembersViewHolder(GroupMembersListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GroupMembersViewHolder, position: Int) {
        val menuItem = items[position]
        if (position % 2 == 1) {
            holder.binding.line.setBackgroundColor(holder.binding.line.context.getColor(hu.ocist.enaplo.login.R.color.light))
        } else {
            holder.binding.line.setBackgroundColor(holder.binding.line.context.getColor(hu.ocist.enaplo.login.R.color.primer_content))
        }
        holder.binding.name.text = menuItem
        holder.binding.index.text = "${position+1}."
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<String>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class GroupMembersViewHolder(
        val binding: GroupMembersListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}