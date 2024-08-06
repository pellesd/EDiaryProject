package hu.ocist.enaplo.login.ui.grade

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.data.requests.HolderGrade
import hu.ocist.enaplo.login.databinding.GradeListRowBinding
import hu.ocist.enaplo.login.ui.seemAsEnabled

class GradesRecyclerViewAdapter(val fragment: GradesFragment) :
    RecyclerView.Adapter<GradesRecyclerViewAdapter.GradesViewHolder>() {
    private val items = mutableListOf<HolderGrade>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesViewHolder {
        this.parent = parent
        return GradesViewHolder(GradeListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: GradesViewHolder, position: Int) {
        val menuItem = items[position]
        holder.binding.name.text = menuItem.student
        initGradeButtons(holder.binding, menuItem)
        holder.setIsRecyclable(false)

        holder.binding.cancel.seemAsEnabled(false)
        when (menuItem.grade) {
            1 -> holder.binding.button1.seemAsEnabled(true)
            2 -> holder.binding.button2.seemAsEnabled(true)
            3 -> holder.binding.button3.seemAsEnabled(true)
            4 -> holder.binding.button4.seemAsEnabled(true)
            5 -> holder.binding.button5.seemAsEnabled(true)
            null -> holder.binding.cancel.seemAsEnabled(true)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<HolderGrade>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class GradesViewHolder(
        val binding: GradeListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)

    private fun setAllGradeButtonAsDisabled(binding: GradeListRowBinding) {
        binding.button1.seemAsEnabled(false)
        binding.button2.seemAsEnabled(false)
        binding.button3.seemAsEnabled(false)
        binding.button4.seemAsEnabled(false)
        binding.button5.seemAsEnabled(false)
        binding.cancel.seemAsEnabled(false)
    }

    private fun initGradeButtons(binding: GradeListRowBinding, menuItem: HolderGrade) {
        setAllGradeButtonAsDisabled(binding)
        binding.cancel.seemAsEnabled(true)

        binding.button1.setOnClickListener {
            setAllGradeButtonAsDisabled(binding)
            binding.button1.seemAsEnabled(true)
            fragment.setGrade(menuItem.studentId, 1)
            menuItem.grade = 1
        }

        binding.button2.setOnClickListener {
            setAllGradeButtonAsDisabled(binding)
            binding.button2.seemAsEnabled(true)
            fragment.setGrade(menuItem.studentId, 2)
            menuItem.grade = 2
        }

        binding.button3.setOnClickListener {
            setAllGradeButtonAsDisabled(binding)
            binding.button3.seemAsEnabled(true)
            fragment.setGrade(menuItem.studentId, 3)
            menuItem.grade = 3
        }

        binding.button4.setOnClickListener {
            setAllGradeButtonAsDisabled(binding)
            binding.button4.seemAsEnabled(true)
            fragment.setGrade(menuItem.studentId, 4)
            menuItem.grade = 4
        }

        binding.button5.setOnClickListener {
            setAllGradeButtonAsDisabled(binding)
            binding.button5.seemAsEnabled(true)
            fragment.setGrade(menuItem.studentId, 5)
            menuItem.grade = 5
        }

        binding.cancel.setOnClickListener {
            setAllGradeButtonAsDisabled(binding)
            binding.cancel.seemAsEnabled(true)
            fragment.setGrade(menuItem.studentId, null)
            menuItem.grade = null
        }
    }
}