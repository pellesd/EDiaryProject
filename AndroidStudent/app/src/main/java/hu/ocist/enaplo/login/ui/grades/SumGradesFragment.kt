package hu.ocist.enaplo.login.ui.grades

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentSumGradesBinding
import hu.ocist.enaplo.login.ui.StudentViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.visible

class SumGradesFragment : Fragment(R.layout.fragment_sum_grades) {

    private lateinit var binding: FragmentSumGradesBinding

    private lateinit var viewModel:  StudentViewModel
    private lateinit var adapterFirst: SumGradesRecyclerViewAdapter
    private lateinit var adapterSecond: SumGradesRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSumGradesBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

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
                is Resource.Failure -> {
                    adapterFirst.empty()
                    adapterSecond.empty()
                    handleApiError(it) { viewModel.sumGrades() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.sumGrades()

        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setTitle(requireContext().getString(R.string.grades))
    }

    private fun initRecyclerView() {
        adapterFirst = SumGradesRecyclerViewAdapter(getString(R.string.first_semester_name), requireActivity() as HomeActivity)
        adapterSecond = SumGradesRecyclerViewAdapter(getString(R.string.second_semester_name), requireActivity() as HomeActivity)
        binding.rvFirst.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFirst.adapter = adapterFirst
        binding.rvSecond.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSecond.adapter = adapterSecond
    }
}