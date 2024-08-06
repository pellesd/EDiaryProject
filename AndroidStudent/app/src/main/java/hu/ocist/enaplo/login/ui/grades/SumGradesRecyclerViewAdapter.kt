package hu.ocist.enaplo.login.ui.grades

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.requests.GradesRequest
import hu.ocist.enaplo.login.data.responses.SumGradesResponse
import hu.ocist.enaplo.login.databinding.SumGradesListRowBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import java.text.DecimalFormat

class SumGradesRecyclerViewAdapter(
    private val semester: String,
    private val activity: HomeActivity
) :
    RecyclerView.Adapter<SumGradesRecyclerViewAdapter.SumGradesViewHolder>() {
    private val items = mutableListOf<SumGradesResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SumGradesViewHolder {
        this.parent = parent
        return SumGradesViewHolder(SumGradesListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SumGradesViewHolder, position: Int) {
        val menuItem = items[position]
        val df = DecimalFormat("#.##")
        holder.binding.subject.text = menuItem.subject
        if (menuItem.average != null)
            holder.binding.average.text = "Átlag: ${df.format(menuItem.average)}"
        else
            holder.binding.average.context.getString(R.string.no_data)

        holder.binding.root.setOnClickListener {
            activity.navigateToGrades(GradesRequest(semester, menuItem.subject))
            activity.setTitle(menuItem.subject)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<SumGradesResponse>) {
        empty()
        for (menuItem in menuItems) {
            if (menuItem.semester == semester)
                items.add(menuItem)
        }
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class SumGradesViewHolder(
        val binding: SumGradesListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}