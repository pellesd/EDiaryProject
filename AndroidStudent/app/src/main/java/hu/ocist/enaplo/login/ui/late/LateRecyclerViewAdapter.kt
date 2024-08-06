package hu.ocist.enaplo.login.ui.late

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.responses.LateResponse
import hu.ocist.enaplo.login.databinding.LateListRowBinding
import hu.ocist.enaplo.login.ui.stringToDateFormat

class LateRecyclerViewAdapter :
    RecyclerView.Adapter<LateRecyclerViewAdapter.LateViewHolder>() {
    private val items = mutableListOf<LateResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LateViewHolder {
        this.parent = parent
        return LateViewHolder(LateListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LateViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.date.text = "${stringToDateFormat(menuItem.date, "yyyy.MM.dd")} ${menuItem.day}"
        holder.binding.numberOfLesson.text = "${menuItem.numberOfLesson}. óra"
        holder.binding.subject.text = menuItem.subject
        holder.binding.minute
            .setTextColor(holder.binding.minute.context.getColor(R.color.red))
        holder.binding.minute.text = "${menuItem.minute} perc"
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<LateResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class LateViewHolder(
        val binding: LateListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}