package hu.ocist.enaplo.login.ui.judgement

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.JudgementResponse
import hu.ocist.enaplo.login.databinding.JudgementListRowBinding
import hu.ocist.enaplo.login.ui.stringToDateFormat

class JudgementRecyclerViewAdapter :
    RecyclerView.Adapter<JudgementRecyclerViewAdapter.JudgementViewHolder>() {
    private val items = mutableListOf<JudgementResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JudgementViewHolder {
        this.parent = parent
        return JudgementViewHolder(JudgementListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: JudgementViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.type.text = menuItem.type.trim()
        holder.binding.date.text = stringToDateFormat(menuItem.date, "yyyy.MM.dd")
        holder.binding.student.text = menuItem.student
        holder.binding.text.text = menuItem.text
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<JudgementResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class JudgementViewHolder(
        val binding: JudgementListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}