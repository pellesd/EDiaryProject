package hu.ocist.enaplo.login.ui.grade

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
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.databinding.FragmentSumGradesBinding
import hu.ocist.enaplo.login.ui.*

class SumGradesFragment : Fragment(R.layout.fragment_sum_grades) {

    private lateinit var binding: FragmentSumGradesBinding
    private lateinit var viewModel:  TeacherViewModel

    private lateinit var adapterFirst: SumGradesRecyclerViewAdapter
    private lateinit var adapterSecond: SumGradesRecyclerViewAdapter

    // groups
    private lateinit var groups: List<IntStringResponse>
    private var groupsSpinner: Spinner? = null
    private var groupId = -1

    // subjects
    private lateinit var subjects: List<IntStringResponse>
    private var subjectsSpinner: Spinner? = null
    private var dividendId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentSumGradesBinding.bind(view)

        viewModel.taughtGroupsResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
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
                    handleApiError(it) { viewModel.gradeTypes() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.subjectsResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    fillSubjectsSpinner(it.value)
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.taughtGroups() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.sumGradesResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapterFirst.empty()
                        adapterSecond.empty()
                    } else {
                        adapterFirst.update(it.value)
                        adapterSecond.update(it.value)
                    }
                }
                is Resource.Failure -> { handleApiError(it) {} }
                is Resource.Loading -> {}
            }
        })

        viewModel.taughtGroups()

        binding.detailsHeaderClosed.setOnClickListener {
            binding.detailsHeaderClosed.visible(null)
            binding.detailsHeaderOpened.visible(true)
        }

        binding.arrowUp.setOnClickListener {
            binding.detailsHeaderClosed.visible(true)
            binding.detailsHeaderOpened.visible(null)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapterFirst = SumGradesRecyclerViewAdapter(
            getString(R.string.first_semester_name),
            requireActivity() as HomeActivity)
        adapterSecond = SumGradesRecyclerViewAdapter(
            getString(R.string.second_semester_name),
            requireActivity() as HomeActivity)
        binding.rvFirst.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFirst.adapter = adapterFirst
        binding.rvSecond.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSecond.adapter = adapterSecond
    }

    private fun fillGroupsSpinner(types: List<IntStringResponse>) {
        groups = types

        val options = ArrayList<String>()
        for (gradeType in groups)
            options.add(gradeType.string.trim())

        groupsSpinner = requireActivity().findViewById(R.id.sum_groups_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        groupsSpinner?.adapter = adapter
        groupsSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent?.getChildAt(0) as TextView?)?.setTextColor(requireContext().getColor(R.color.primer_content))
                groupId = groups[position].int
                viewModel.subjects(groupId)
            }
        }
    }
    private fun fillSubjectsSpinner(types: List<IntStringResponse>) {
        subjects = types
        val options = ArrayList<String>()
        for (subject in subjects)
            options.add(subject.string.trim())

        subjectsSpinner = requireActivity().findViewById(R.id.sum_subjects_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        subjectsSpinner?.adapter = adapter
        subjectsSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                (parent?.getChildAt(0) as TextView?)?.setTextColor(requireContext().getColor(R.color.primer_content))
                dividendId = subjects[position].int
                adapterFirst.subject = subjects[position].string
                adapterSecond.subject = subjects[position].string
                viewModel.sumGrades(dividendId)
            }
        }
    }
}