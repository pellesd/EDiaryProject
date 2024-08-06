package hu.ocist.enaplo.login.ui.canteen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentWithArrowsBinding
import hu.ocist.enaplo.login.ui.StudentViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.visible
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CanteenFragment : Fragment(R.layout.fragment_with_arrows) {

    private lateinit var binding: FragmentWithArrowsBinding
    private lateinit var viewModel:  StudentViewModel
    private var localDate = LocalDate.now()
    private lateinit var adapter: CanteenRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentWithArrowsBinding.bind(view)
        // set visible arrows
        binding.buttonToolbar.leftArrow.visible(true)
        binding.buttonToolbar.rightArrow.visible(true)

        viewModel.canteenResponse.observe(viewLifecycleOwner, Observer {
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
                    handleApiError(it) { sendCanteen() }
                }
                is Resource.Loading -> {}
            }
        })

        sendCanteen()

        binding.buttonToolbar.leftArrow.setOnClickListener {
            localDate = localDate.minusDays(1)
            sendCanteen()
        }

        binding.buttonToolbar.rightArrow.setOnClickListener {
            localDate = localDate.plusDays(1)
            sendCanteen()
        }

        initRecyclerView()
    }

    private fun sendCanteen() {
        viewModel.canteen(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        (activity as HomeActivity).setTitle(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    private fun initRecyclerView() {
        adapter = CanteenRecyclerViewAdapter()
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}