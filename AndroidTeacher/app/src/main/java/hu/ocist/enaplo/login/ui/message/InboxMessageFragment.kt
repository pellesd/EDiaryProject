package hu.ocist.enaplo.login.ui.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentSimpleBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.TeacherViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.visible

class InboxMessageFragment : Fragment(R.layout.fragment_simple) {

    private lateinit var binding: FragmentSimpleBinding

    private lateinit var viewModel:  TeacherViewModel
    private lateinit var adapter_inbox: TeacherMessageRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSimpleBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.nameResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    ((requireActivity() as HomeActivity).setName(it.value.value))
                }
                is Resource.Failure -> { viewModel.name() }
                is Resource.Loading -> {}
            }
        })
        viewModel.inboxMessagesResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapter_inbox.empty()
                        binding.textMessage.visibility = View.VISIBLE
                    } else {
                        adapter_inbox.update(it.value)
                        binding.textMessage.visibility = View.INVISIBLE
                    }
                }
                is Resource.Failure -> {
                    adapter_inbox.empty()
                    handleApiError(it) { viewModel.inboxMessages() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.inboxMessages()
        viewModel.name()

        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        (activity as HomeActivity).setTitleToDefault()
    }

    private fun initRecyclerView() {
        adapter_inbox = TeacherMessageRecyclerViewAdapter(activity as HomeActivity)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter_inbox
    }
}