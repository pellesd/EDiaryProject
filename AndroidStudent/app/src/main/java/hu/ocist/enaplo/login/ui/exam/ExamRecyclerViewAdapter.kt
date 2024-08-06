package hu.ocist.enaplo.login.ui.exam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.ExamResponse
import hu.ocist.enaplo.login.databinding.ExamListRowBinding
import hu.ocist.enaplo.login.ui.stringToDateFormat

class ExamRecyclerViewAdapter :
    RecyclerView.Adapter<ExamRecyclerViewAdapter.ExamViewHolder>() {
    private val items = mutableListOf<ExamResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        this.parent = parent
        return ExamViewHolder(ExamListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.date.text = stringToDateFormat(menuItem.date, "yyyy.MM.dd")
        holder.binding.teacher.text = menuItem.teacher
        holder.binding.exam.text = menuItem.text
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<ExamResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class ExamViewHolder(
        val binding: ExamListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}