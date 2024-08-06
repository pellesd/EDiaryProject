package hu.ocist.enaplo.login.ui.registerLesson

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.RegisterLessonRequest
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.data.responses.RegisterLessonResponse
import hu.ocist.enaplo.login.databinding.FragmentRegisterLessonBinding
import hu.ocist.enaplo.login.ui.*
import java.util.*


class RegisterLessonFragment : Fragment(R.layout.fragment_register_lesson) {

    private lateinit var binding: FragmentRegisterLessonBinding
    private lateinit var viewModel:  TeacherViewModel

    // TaughtGroups
    private lateinit var taughtGroups: List<IntStringResponse>
    private var groupsSpinner: Spinner? = null

    // Teachers
    private lateinit var teachers: MutableList<IntStringResponse>
    private var teachersSpinner: Spinner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentRegisterLessonBinding.bind(view)


        (activity as HomeActivity).setTitle(requireContext().getString(R.string.registerLessonTitle))

        viewModel.getRegisterLessonResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.absence.enable(it is Resource.Success)
            binding.late.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {

                    (requireActivity() as HomeActivity).registerLesson = it.value
                    var registerLesson = (requireActivity() as HomeActivity).registerLesson
                    if (registerLesson != null) {
                        viewModel.taughtGroups()
                        viewModel.teachers()

                        //bindings
                        binding.date.text = stringToDateFormat(registerLesson.date, "yyyy.MM.dd")
                        binding.day.text = registerLesson.day.trim()
                        binding.number.text = "${registerLesson.numberOfLesson}. óra"
                        binding.subject.text = registerLesson.subject.trim()
                        binding.deletedSwitch.isChecked = registerLesson.deleted
                        binding.gradeSwitch.isChecked = registerLesson.shouldGrade
                        binding.titleText.setText(registerLesson.lessonDescription)
                    } else {
                        viewModel.getRegisterLesson((requireActivity() as HomeActivity).lessonKeysRequest)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) {
                        viewModel.getRegisterLesson((requireActivity() as HomeActivity).lessonKeysRequest)
                    }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.taughtGroupsResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            binding.absence.enable(it is Resource.Success)
            binding.late.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // feltölteni spinnert az opciókkal
                        fillGroupsSpinner(it.value)
                        (requireActivity() as HomeActivity).registerLesson?.let{ regLes ->
                            val position = getPosById(
                                taughtGroups,
                                regLes.groupId)
                            binding.groupSpinner.setSelection(position)
                        }
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.taughtGroups() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.teachersResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            binding.absence.enable(it is Resource.Success)
            binding.late.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // feltölteni spinnert az opciókkal
                        fillTeachersSpinner(it.value as MutableList<IntStringResponse>)
                        (requireActivity() as HomeActivity).registerLesson?.let { regLes ->
                            val position = getPosById(
                                teachers,
                                regLes.teacherId
                            )
                            binding.teacherSpinner.setSelection(position)
                        }
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.teachers() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.postRegisterLessonResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
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

        viewModel.getRegisterLesson((requireActivity() as HomeActivity).lessonKeysRequest)

        binding.titleText.addTextChangedListener {
            binding.submit.enable(it.toString().isNotEmpty())
            binding.absence.enable(it.toString().isNotEmpty())
            binding.late.enable(it.toString().isNotEmpty())
            (requireActivity() as HomeActivity).registerLesson?.lessonDescription = binding.titleText.text.toString()
        }

        binding.submit.setOnClickListener {
            val registerLesson = (requireActivity() as HomeActivity).registerLesson
            registerLesson?.let {
                viewModel.postRegisterLesson(
                    RegisterLessonRequest (
                        it.lessonId,
                        it.day,
                        it.numberOfLesson,
                        it.date,
                        it.teacherId,
                        it.groupId,
                        it.dividendId,
                        it.subjectId,
                        it.lessonDescriptionId,
                        it.lessonDescription ?: "", // should never happen
                        binding.deletedSwitch.isChecked,
                        it.dated,
                        binding.gradeSwitch.isChecked
                    )
                )
            }
        }

        binding.absence.setOnClickListener {
            val registerLesson = (requireActivity() as HomeActivity).registerLesson
            registerLesson?.let {
                (requireActivity() as HomeActivity).navigateToAbsence(
                    it.registerLessonId,
                    it.dividendId,
                    stringToDateFormat(it.date, "yyyy-MM-dd")
                )
            }
        }

        binding.late.setOnClickListener {
            val registerLesson = (requireActivity() as HomeActivity).registerLesson
            registerLesson?.let {
                (requireActivity() as HomeActivity).navigateToLate(
                    it.registerLessonId,
                    it.dividendId,
                    stringToDateFormat(it.date, "yyyy-MM-dd")
                )
            }
        }
    }

    private fun fillGroupsSpinner(_groups: List<IntStringResponse>) {
        taughtGroups = _groups

        val options = ArrayList<String>()
        for (group in taughtGroups)
            options.add(group.string.trim())

        groupsSpinner = requireActivity().findViewById(R.id.group_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        groupsSpinner?.adapter = adapter
        groupsSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                (requireActivity() as HomeActivity).registerLesson?.groupId = getIntByList(taughtGroups, item)
            }
        }
    }
    private fun fillTeachersSpinner(_teachers: MutableList<IntStringResponse>) {
        teachers = _teachers
        (requireActivity() as HomeActivity).registerLesson?.let { regLes ->
            teachers.add(0, IntStringResponse(
                regLes.teacherId,
                regLes.teacher))
        }

        val options = ArrayList<String>()
        for (teacher in teachers)
            options.add(teacher.string.trim())

        teachersSpinner = requireActivity().findViewById(R.id.teacher_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        teachersSpinner?.adapter = adapter
        teachersSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                (requireActivity() as HomeActivity).registerLesson?.teacherId =
                    getIntByList(teachers, item)
            }
        }
    }

    private fun getPosById(list: List<IntStringResponse>, id: Int): Int {
        for (i in list.indices) {
            if (list[i].int == id) {
                return i
            }
        }
        return -1
    }

    private fun getIntByList(list: List<IntStringResponse>, string: String): Int {
        for (type in list) {
            if (type.string.trim() == string) {
                return type.int
            }
        }
        return -1
    }
}