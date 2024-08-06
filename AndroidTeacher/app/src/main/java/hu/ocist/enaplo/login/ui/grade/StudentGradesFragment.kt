package hu.ocist.enaplo.login.ui.grade

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.GradesRequest
import hu.ocist.enaplo.login.databinding.FragmentSimpleBinding
import hu.ocist.enaplo.login.ui.*

class StudentGradesFragment : Fragment(R.layout.fragment_simple) {

    private lateinit var binding: FragmentSimpleBinding

    private lateinit var viewModel: TeacherViewModel
    private lateinit var adapter: StudentGradesRecyclerViewAdapter
    private lateinit var grade: GradesRequest

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSimpleBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.gradesResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapter.empty()
                        binding.textMessage.visibility = View.VISIBLE
                    } else {
                        adapter.update(it.value)
                        binding.textMessage.visibility = View.INVISIBLE
                    }
                }
                is Resource.Failure -> {
                    adapter.empty()
                    handleApiError(it) { viewModel.grades(grade) }
                }
                is Resource.Loading -> {}
            }
        })

        // get grade details
        (requireActivity() as HomeActivity).grade?.let {
            grade = it
            viewModel.grades(grade)
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = StudentGradesRecyclerViewAdapter()
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}