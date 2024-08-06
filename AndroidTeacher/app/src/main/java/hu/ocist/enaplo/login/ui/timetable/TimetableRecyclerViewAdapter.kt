package hu.ocist.enaplo.login.ui.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.requests.LessonKeysRequest
import hu.ocist.enaplo.login.data.responses.TimetableResponse
import hu.ocist.enaplo.login.databinding.TimetableListRowBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.stringToDateFormat

class TimetableRecyclerViewAdapter(
    private val activity: HomeActivity
) :
    RecyclerView.Adapter<TimetableRecyclerViewAdapter.TimetableViewHolder>() {
    private val items = mutableListOf<TimetableResponse>()
    private lateinit var parent: ViewGroup
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        this.parent = parent
        return TimetableViewHolder(TimetableListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    private fun set_timetable_clock(holder: TimetableViewHolder, menuItem: TimetableResponse) {
        val classFrom = listOf("7:45", "8:40", "9:35", "10:40", "11:35", "12:30", "13:25", "14:15")
        val classTo = listOf("8:30", "9:25", "10:20", "11:25", "12:20", "13:15", "14:10", "15:00")
        val index = menuItem.numberOfLesson - 1
        if (index < classFrom.size)
            holder.binding.classFrom.text = classFrom[index]
        if (index < classFrom.size)
            holder.binding.classTo.text = classTo[index]
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val menuItem = items[position]
        set_timetable_clock(holder, menuItem)
        holder.binding.classNumber.text = "${menuItem.numberOfLesson}. óra"
        holder.binding.className.text = menuItem.group
        holder.binding.classType.text = menuItem.lesson
        holder.binding.teacher.text = menuItem.teacher
        holder.binding.classWhere.text = "Terem: ${menuItem.room ?: "Nincs adat"}"

        holder.binding.arrow.setOnClickListener {
            navToRegisterLesson(menuItem)
        }

        holder.binding.root.setOnClickListener {
            navToRegisterLesson(menuItem)
        }

        holder.binding.grade.setOnClickListener {
            activity.navigateToAddGrade(
                LessonKeysRequest(
                    stringToDateFormat(menuItem.date, "yyyy-MM-dd"),
                    menuItem.numberOfLesson,
                    menuItem.dividendId,
                    menuItem.groupId
                )
            )
            activity.setTitle(holder.binding.root.context.getString(R.string.addGradeTitle))
        }

        holder.binding.grades.setOnClickListener {
            activity.navigateToAddGrades(
                LessonKeysRequest(
                    stringToDateFormat(menuItem.date, "yyyy-MM-dd"),
                    menuItem.numberOfLesson,
                    menuItem.dividendId,
                    menuItem.groupId
                )
            )
            activity.setTitle(holder.binding.root.context.getString(R.string.addGradesTitle))
        }
    }

    private fun navToRegisterLesson(menuItem: TimetableResponse) {
        activity.navigateToRegisterLesson(
            LessonKeysRequest(
                stringToDateFormat(menuItem.date, "yyyy-MM-dd"),
                menuItem.numberOfLesson,
                menuItem.dividendId,
                menuItem.groupId
            )
        )
        activity.setTitle(R.string.registerLessonTitle)
    }

    override fun getItemCount(): Int = items.size

    fun update(menuItems: List<TimetableResponse>) {
        empty()
        items.addAll(menuItems)
        notifyDataSetChanged()
    }

    fun empty() {
        items.clear()
        notifyDataSetChanged()
    }

    inner class TimetableViewHolder(
        val binding: TimetableListRowBinding
        ) : RecyclerView.ViewHolder(binding.root)
}