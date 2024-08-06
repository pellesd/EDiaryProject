package hu.ocist.enaplo.login.ui.timetable

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.databinding.FragmentWithArrowsBinding
import hu.ocist.enaplo.login.ui.TeacherViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.visible
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TimetableFragment : Fragment(R.layout.fragment_with_arrows) {

    private lateinit var binding: FragmentWithArrowsBinding
    private lateinit var viewModel:  TeacherViewModel
    private var localDate = LocalDate.now()
    private lateinit var adapter: TimetableRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentWithArrowsBinding.bind(view)
        // set visible arrows
        binding.buttonToolbar.leftArrow.visible(true)
        binding.buttonToolbar.rightArrow.visible(true)

        viewModel.timetableResponse.observe(viewLifecycleOwner, Observer {
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
                    handleApiError(it) { sendTimetable() }
                }
                is Resource.Loading -> {}
            }
        })

        sendTimetable()

        binding.buttonToolbar.leftArrow.setOnClickListener {
            localDate = localDate.minusDays(1)
            sendTimetable()
        }

        binding.buttonToolbar.rightArrow.setOnClickListener {
            localDate = localDate.plusDays(1)
            sendTimetable()
        }

        initRecyclerView()
    }

    private fun sendTimetable() {
        viewModel.timetable(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        (activity as HomeActivity).setTitle(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }


    override fun onResume() {
        super.onResume()
        (requireActivity() as HomeActivity).setTitle(requireContext().getString(R.string.timetable))
    }

    private fun initRecyclerView() {
        adapter = TimetableRecyclerViewAdapter(requireActivity() as HomeActivity)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}