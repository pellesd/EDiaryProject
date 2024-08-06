package hu.ocist.enaplo.login.ui.grade

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.AddGradeRequest
import hu.ocist.enaplo.login.data.requests.LessonKeysRequest
import hu.ocist.enaplo.login.data.responses.GradeTypeResponse
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.databinding.FragmentGradeBinding
import hu.ocist.enaplo.login.ui.*

class GradeFragment : Fragment(R.layout.fragment_grade) {

    private lateinit var binding: FragmentGradeBinding
    private lateinit var viewModel:  TeacherViewModel

    // gradeTypes
    private lateinit var gradeTypes: List<GradeTypeResponse>
    private var typesSpinner: Spinner? = null
    private var typeShort = ""

    // Student
    private lateinit var students: List<IntStringResponse>
    private var studentSpinner: Spinner? = null
    private var studentId = -1

    // Multiplier
    private var multiplierSpinner: Spinner? = null
    private var multiplier = -1

    // selectedGrade
    private var selectedGrade = 5

    private lateinit var lessonData: LessonKeysRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lessonData = (requireActivity() as HomeActivity).lessonKeysRequest

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentGradeBinding.bind(view)

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
                        // feltölteni spinnert az opciókkal
                        fillStudentsSpinner(it.value)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.taughtGroups() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.sendGradeResponse.observe(viewLifecycleOwner, Observer {
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
        initGradeButtons()

        binding.submit.setOnClickListener() {
            viewModel.sendGrade(
                AddGradeRequest(
                    lessonData.dividendId,
                    selectedGrade,
                    selectedGrade.toString()+typeShort,
                    studentId,
                    lessonData.date,
                    binding.detailsText.text.toString(),
                    multiplier
                )
            )
        }
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
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                typeShort = getShortByListGrade(gradeTypes, item)
            }
        }
    }
    private fun fillStudentsSpinner(studentsList: List<IntStringResponse>) {
        students = studentsList

        val options = ArrayList<String>()
        for (student in students)
            options.add(student.string.trim())

        studentSpinner = requireActivity().findViewById(R.id.students_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        studentSpinner?.adapter = adapter
        studentSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                studentId = getIntByList(students, item)
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
                multiplier = position + 1
            }
        }
    }

    private fun getIntByList(list: List<IntStringResponse>, string: String): Int {
        var id = -1
        for (type in list) {
            if (type.string.trim() == string) {
                id = type.int
                break
            }
        }
        return id
    }
    private fun getShortByListGrade(list: List<GradeTypeResponse>, string: String): String {
        for (type in list) {
            if (type.name.trim() == string) {
                return type.shortName
            }
        }
        return ""
    }

    private fun setAllGradeButtonAsDisabled() {
        binding.button1.seemAsEnabled(false)
        binding.button2.seemAsEnabled(false)
        binding.button3.seemAsEnabled(false)
        binding.button4.seemAsEnabled(false)
        binding.button5.seemAsEnabled(false)
    }

    private fun initGradeButtons() {
        setAllGradeButtonAsDisabled()
        binding.button5.seemAsEnabled(true)

        binding.button1.setOnClickListener {
            setAllGradeButtonAsDisabled()
            binding.button1.seemAsEnabled(true)
            selectedGrade = 1
        }

        binding.button2.setOnClickListener {
            setAllGradeButtonAsDisabled()
            binding.button2.seemAsEnabled(true)
            selectedGrade = 2
        }

        binding.button3.setOnClickListener {
            setAllGradeButtonAsDisabled()
            binding.button3.seemAsEnabled(true)
            selectedGrade = 3
        }

        binding.button4.setOnClickListener {
            setAllGradeButtonAsDisabled()
            binding.button4.seemAsEnabled(true)
            selectedGrade = 4
        }

        binding.button5.setOnClickListener {
            setAllGradeButtonAsDisabled()
            binding.button5.seemAsEnabled(true)
            selectedGrade = 5
        }
    }
}