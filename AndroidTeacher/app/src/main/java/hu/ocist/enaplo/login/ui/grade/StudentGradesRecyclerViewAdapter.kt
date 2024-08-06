package hu.ocist.enaplo.login.ui.grade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.GradesResponse
import hu.ocist.enaplo.login.databinding.StudentGradesListRowBinding
import hu.ocist.enaplo.login.ui.stringToDateFormat
import hu.ocist.enaplo.login.ui.visible

class StudentGradesRecyclerViewAdapter() :
    RecyclerView.Adapter<StudentGradesRecyclerViewAdapter.GradesViewHolder>() {
    private val items = mutableListOf<GradesResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesViewHolder {
        this.parent = parent
        return GradesViewHolder(StudentGradesListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GradesViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.gardeString.text = menuItem.gradeString
        holder.binding.teacher.text = menuItem.teacher
        holder.binding.date.text = stringToDateFormat(menuItem.date, "yyyy.MM.dd")
        holder.binding.text.text = menuItem.text
        if (menuItem.text.isNotEmpty())
            holder.binding.text.visible(true)
        else
            holder.binding.text.visible(null)
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<GradesResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class GradesViewHolder(
        val binding: StudentGradesListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}