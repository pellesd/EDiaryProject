package hu.ocist.enaplo.login.ui.groupMembers

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentSimpleBinding
import hu.ocist.enaplo.login.ui.*

class GroupMemberFragment : Fragment(R.layout.fragment_simple) {

    private lateinit var binding: FragmentSimpleBinding

    private lateinit var viewModel: TeacherViewModel
    private lateinit var adapter: GroupMembersRecyclerViewAdapter
    private var messageId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSimpleBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.groupMembersResponse.observe(viewLifecycleOwner, Observer {
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
                    handleApiError(it) { viewModel.groupMessageMembers(messageId) }
                }
                is Resource.Loading -> {}
            }
        })

        // get group details
        messageId = (requireActivity() as HomeActivity).messageId
        viewModel.groupMessageMembers(messageId)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = GroupMembersRecyclerViewAdapter()
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}