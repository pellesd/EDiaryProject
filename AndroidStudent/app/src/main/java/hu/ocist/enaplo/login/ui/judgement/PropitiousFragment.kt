package hu.ocist.enaplo.login.ui.judgement

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentSimpleBinding
import hu.ocist.enaplo.login.ui.StudentViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.visible
import hu.ocist.enaplo.login.ui.HomeActivity

class PropitiousFragment : Fragment(R.layout.fragment_simple) {

    private lateinit var binding: FragmentSimpleBinding

    private lateinit var viewModel:  StudentViewModel
    private lateinit var adapter: JudgementRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSimpleBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.propitiousResponse.observe(viewLifecycleOwner, Observer {
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
                    handleApiError(it) { viewModel.propitious() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.propitious()

        initRecyclerView()
    }
    /*
    override fun onResume() {
        super.onResume()
        viewModel.propitious()
    }*/

private fun initRecyclerView() {
    adapter = JudgementRecyclerViewAdapter()
    binding.rv.layoutManager = LinearLayoutManager(requireContext())
    binding.rv.adapter = adapter
}
}