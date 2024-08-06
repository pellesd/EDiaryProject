package hu.ocist.enaplo.login.ui.judgement

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
import hu.ocist.enaplo.login.data.requests.NewJudgementRequest
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.databinding.FragmentSendJudgementBinding
import hu.ocist.enaplo.login.ui.*


class SendPropitiousFragment : Fragment(R.layout.fragment_send_judgement) {

    private lateinit var binding: FragmentSendJudgementBinding
    private lateinit var viewModel:  TeacherViewModel

    // PropitiousTypes
    private lateinit var propitiousTypes: List<IntStringResponse>
    private var typesSpinner: Spinner? = null
    private var typeId = -1

    // TaughtGroups
    private lateinit var taughtGroups: List<IntStringResponse>
    private var groupsSpinner: Spinner? = null
    private var groupId = -1

    // Student
    private lateinit var students: List<IntStringResponse>
    private var studentSpinner: Spinner? = null
    private var studentId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentSendJudgementBinding.bind(view)

        viewModel.propitiousTypes.observe(viewLifecycleOwner, Observer {
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
                    handleApiError(it) { viewModel.propitiousTypes() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.taughtGroupsResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // feltölteni spinnert az opciókkal
                        fillGroupsSpinner(it.value)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.taughtGroups() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.taughtGroupMembersResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success
                    && binding.editTextTextMultiLine.text.isNotEmpty())
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
        viewModel.propitiousResponse.observe(viewLifecycleOwner, Observer {
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

        viewModel.propitiousTypes()
        viewModel.taughtGroups()



        binding.editTextTextMultiLine.addTextChangedListener {
            binding.submit.enable(it.toString().isNotEmpty())
        }

        binding.submit.setOnClickListener {
            val text = binding.editTextTextMultiLine.text.trim().toString()
            viewModel.propitiousToStudent(NewJudgementRequest(
                studentId, text, typeId
            ))
        }
    }

    private fun fillTypesSpinner(types: List<IntStringResponse>) {
        propitiousTypes = types

        val options = ArrayList<String>()
        for (propitiousType in propitiousTypes)
            options.add(propitiousType.string.trim())

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
                typeId = getIntByList(propitiousTypes, item)
            }
        }
    }
    private fun fillGroupsSpinner(groups: List<IntStringResponse>) {
        taughtGroups = groups

        val options = ArrayList<String>()
        for (taughtGroup in taughtGroups)
            options.add(taughtGroup.string.trim())

        groupsSpinner = requireActivity().findViewById(R.id.groups_spinner)
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
                groupId = getIntByList(taughtGroups, item)
                viewModel.groupMembers(groupId)
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
}