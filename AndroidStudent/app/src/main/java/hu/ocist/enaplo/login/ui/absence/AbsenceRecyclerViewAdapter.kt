package hu.ocist.enaplo.login.ui.absence

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.responses.AbsenceResponse
import hu.ocist.enaplo.login.databinding.AbsenceListRowBinding
import hu.ocist.enaplo.login.ui.stringToDateFormat

class AbsenceRecyclerViewAdapter :
    RecyclerView.Adapter<AbsenceRecyclerViewAdapter.AbsenceViewHolder>() {
    private val items = mutableListOf<AbsenceResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenceViewHolder {
        this.parent = parent
        return AbsenceViewHolder(AbsenceListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AbsenceViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.date.text = "${stringToDateFormat(menuItem.date, "yyyy.MM.dd")} ${menuItem.day}"
        holder.binding.numberOfLesson.text = "${menuItem.numberOfLesson}. óra"
        holder.binding.subject.text = menuItem.subject
        if (menuItem.notPending) {
            if (menuItem.normalAuthorizedAbsence) {
                holder.binding.status.text =
                    parent.context.getString(R.string.authorized_absence)
                holder.binding.status
                    .setTextColor(holder.binding.status.context.getColor(R.color.green))
            } else if (menuItem.schoolInterest) {
                holder.binding.status.text =
                    parent.context.getString(R.string.school_interest_absence)
                holder.binding.status
                    .setTextColor(holder.binding.status.context.getColor(R.color.purple))
            }
            else {
                holder.binding.status.text =
                    parent.context.getString(R.string.unauthorized_interest)
                holder.binding.status
                    .setTextColor(holder.binding.status.context.getColor(R.color.red))
            }
        }
        else {
            holder.binding.status.text =
                parent.context.getString(R.string.pending)
            holder.binding.status
                .setTextColor(holder.binding.status.context.getColor(R.color.blue))
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<AbsenceResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class AbsenceViewHolder(
        val binding: AbsenceListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}