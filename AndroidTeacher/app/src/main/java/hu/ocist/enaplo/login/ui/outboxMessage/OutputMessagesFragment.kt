package hu.ocist.enaplo.login.ui.outboxMessage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentOutputMessagesBinding
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.TeacherViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.message.TeacherMessageRecyclerViewAdapter
import hu.ocist.enaplo.login.ui.visible

class OutputMessagesFragment : Fragment(R.layout.fragment_output_messages) {

    private lateinit var binding: FragmentOutputMessagesBinding

    private lateinit var viewModel: TeacherViewModel
    private lateinit var adapterFirst: TeacherMessageRecyclerViewAdapter
    private lateinit var adapterSecond: OutputGroupRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentOutputMessagesBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.outboxMessagesResponse.observe(viewLifecycleOwner, Observer {
            binding.loadingFirst.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapterFirst.empty()
                    } else {
                        adapterFirst.update(it.value)
                    }
                }
                is Resource.Failure -> {
                    adapterFirst.empty()
                    handleApiError(it) { viewModel.outboxMessages() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.messagesToGroupResponse.observe(viewLifecycleOwner, Observer {
            binding.loadingSecond.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapterSecond.empty()
                    } else {
                        adapterSecond.update(it.value)
                    }
                }
                is Resource.Failure -> {
                    adapterSecond.empty()
                    handleApiError(it) { viewModel.messagesSentToGroup() }
                }
                is Resource.Loading -> {}
            }
        })

        sendRequest()
        initRecyclerView()

        binding.buttonTeacher.setOnClickListener {
            (activity as HomeActivity).navigateToSendMessageToTeacher()
            (activity as HomeActivity).setTitle(getString(R.string.newMessage))
        }

        binding.buttonStudent.setOnClickListener {
            (activity as HomeActivity).navigateToSendMessageToGroup()
            (activity as HomeActivity).setTitle("Új üzenet")
        }
    }

    private fun sendRequest() {
        viewModel.outboxMessages()
        viewModel.messagesSentToGroup()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setTitle(requireContext().getString(R.string.sentMessageTitle))
    }

    private fun initRecyclerView() {
        adapterFirst = TeacherMessageRecyclerViewAdapter(requireActivity() as HomeActivity)
        adapterSecond = OutputGroupRecyclerViewAdapter(requireActivity() as HomeActivity)
        binding.rvFirst.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFirst.adapter = adapterFirst
        binding.rvSecond.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSecond.adapter = adapterSecond
    }
}