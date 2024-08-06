package hu.ocist.enaplo.login.ui.grade

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.AddGradeRequest
import hu.ocist.enaplo.login.data.requests.HolderGrade
import hu.ocist.enaplo.login.data.requests.LessonKeysRequest
import hu.ocist.enaplo.login.data.responses.GradeTypeResponse
import hu.ocist.enaplo.login.databinding.FragmentGradesBinding
import hu.ocist.enaplo.login.ui.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class GradesFragment : Fragment(R.layout.fragment_grades) {

    private lateinit var binding: FragmentGradesBinding
    private lateinit var viewModel:  TeacherViewModel

    // gradeTypes
    private lateinit var gradeTypes: List<GradeTypeResponse>
    private var typesSpinner: Spinner? = null
    private var typeShort = ""

    // Multiplier
    private var multiplierSpinner: Spinner? = null
    private var multiplier = -1

    // studentsWithGrades
    private lateinit var studentsWithGrades: MutableList<HolderGrade>
    private lateinit var adapter: GradesRecyclerViewAdapter

    private lateinit var lessonData: LessonKeysRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lessonData = (requireActivity() as HomeActivity).lessonKeysRequest

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentGradesBinding.bind(view)

        viewModel.gradeTypesResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // feltölteni spinnert az opciókkal
                        fillTypesSpinner(it.value)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.gradeTypes() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.taughtGroupMembersResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        studentsWithGrades = mutableListOf()

                        for(item in it.value)
                            studentsWithGrades.add(HolderGrade(
                                item.string,
                                null,
                                item.int
                            ))
                        adapter.update(studentsWithGrades)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.taughtGroups() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.sendGradesResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt!")
                    } else {
                        // kiírni és back
                        getView()?.snackbar(it.value.value)
                        (requireActivity() as HomeActivity).popBackStackNavController()
                    }
                }
                is Resource.Failure -> { handleApiError(it) {} }
                is Resource.Loading -> {}
            }
        })

        viewModel.gradeTypes()
        viewModel.groupMembers(lessonData.groupId)
        fillMultiplierSpinner()

        binding.detailsHeaderClosed.setOnClickListener {
            binding.detailsHeaderClosed.visible(null)
            binding.detailsHeaderOpened.visible(true)
        }

        binding.arrowUp.setOnClickListener {
            binding.detailsHeaderClosed.visible(true)
            binding.detailsHeaderOpened.visible(null)
        }

        binding.submit.setOnClickListener() {

            val grades = mutableListOf<AddGradeRequest>()

            for(student in studentsWithGrades) {
                var text: String? = binding.detailsText.text.toString()
                if (text!!.isEmpty())
                    text = null
                if (student.grade != null) {
                    grades.add(
                        AddGradeRequest(
                            lessonData.dividendId,
                            student.grade!!,
                            student.grade.toString()+typeShort,
                            student.studentId,
                            lessonData.date,
                            text,
                            multiplier
                        )
                    )
                }
            }

            viewModel.sendGrades(grades)
        }

        val localDate = LocalDate.now()
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
            .toString()
            .replace("-", ".")

        binding.datePickerButton.text = localDate

        binding.datePickerButton.setOnClickListener {
            dataPickerListener()
        }

        binding.dataPickerEdit.setOnClickListener {
            dataPickerListener()
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = GradesRecyclerViewAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }

    fun setGrade(id: Int, grade: Int?) {
        for(item in studentsWithGrades) {
            if (item.studentId == id) {
                item.grade = grade
                break
            }
        }
    }

    private fun dataPickerListener() {
        val date = lessonData.date.split("-")
        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                var sMonth = (mMonth + 1).toString()
                if (sMonth.length < 2)
                    sMonth = "0$sMonth"
                var sDay= mDay.toString()
                if (sDay.length < 2)
                    sDay = "0$sDay"
                lessonData.date = "${mYear}-$sMonth-$sDay"
                binding.datePickerButton.text = "${mYear}.$sMonth.$sDay"
            }, date[0].toInt(), date[1].toInt() - 1, date[2].toInt())
        // show
        dpd.show()
    }

    private fun fillTypesSpinner(types: List<GradeTypeResponse>) {
        gradeTypes = types

        val options = ArrayList<String>()
        for (gradeType in gradeTypes)
            options.add(gradeType.name.trim())

        typesSpinner = requireActivity().findViewById(R.id.types_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        typesSpinner?.adapter = adapter
        typesSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent?.getChildAt(0) as TextView?)?.setTextColor(requireContext().getColor(R.color.primer_content))
                val item: String = parent?.getItemAtPosition(position).toString()
                typeShort = getShortByListGrade(gradeTypes, item)
            }
        }
    }
    private fun fillMultiplierSpinner() {
        val options = ArrayList<String>()
        for (multiplier in 1..6)
            options.add(multiplier.toString())

        multiplierSpinner = requireActivity().findViewById(R.id.multiplier_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        multiplierSpinner?.adapter = adapter
        multiplierSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent?.getChildAt(0) as TextView?)?.setTextColor(requireContext().getColor(R.color.primer_content))
                multiplier = position + 1
            }
        }
    }

    private fun getShortByListGrade(list: List<GradeTypeResponse>, string: String): String {
        for (type in list) {
            if (type.name.trim() == string) {
                return type.shortName
            }
        }
        return ""
    }
}