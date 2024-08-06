package hu.ocist.enaplo.login.ui.judgement

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.visible
import hu.ocist.enaplo.login.databinding.FragmentWithFabBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.TeacherViewModel

class AdmonitoryFragment : Fragment(R.layout.fragment_with_fab) {

    private lateinit var binding: FragmentWithFabBinding

    private lateinit var viewModel:  TeacherViewModel
    private lateinit var adapter: JudgementRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWithFabBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.admonitoriesResponse.observe(viewLifecycleOwner, Observer {
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
                    handleApiError(it) { viewModel.admonitories() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.admonitories()

        initRecyclerView()

        binding.toolbar.fab.setOnClickListener {
            (requireActivity() as HomeActivity).navigateToAddAdmonitory()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setTitle(requireContext().getString(R.string.admonitory))
    }

    private fun initRecyclerView() {
        adapter = JudgementRecyclerViewAdapter()
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}