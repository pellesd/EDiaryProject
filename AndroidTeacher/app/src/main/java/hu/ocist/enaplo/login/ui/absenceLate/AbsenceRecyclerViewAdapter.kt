package hu.ocist.enaplo.login.ui.absenceLate

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.responses.MissingResponse
import hu.ocist.enaplo.login.databinding.AbsenceListRowBinding

class AbsenceRecyclerViewAdapter(val fragment: AbsenceFragment) :
    RecyclerView.Adapter<AbsenceRecyclerViewAdapter.AbsenceViewHolder>() {
    private val items = mutableListOf<MissingResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsenceViewHolder {
        this.parent = parent
        return AbsenceViewHolder(AbsenceListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AbsenceViewHolder, position: Int) {
        val menuItem = items[position]
        if (position % 2 == 1) {
            holder.binding.line.setBackgroundColor(holder.binding.line.context.getColor(hu.ocist.enaplo.login.R.color.light))
        } else {
            holder.binding.line.setBackgroundColor(holder.binding.line.context.getColor(hu.ocist.enaplo.login.R.color.primer_content))
        }
        holder.binding.name.text = menuItem.string
        holder.binding.index.text = "${position+1}."
        holder.binding.absenceSwitch.isChecked = menuItem.bool

        holder.binding.absenceSwitch.setOnClickListener {
            menuItem.bool = !menuItem.bool
            fragment.setAbsence(menuItem.int, menuItem.bool)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<MissingResponse>) {
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