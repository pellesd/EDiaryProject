package hu.ocist.enaplo.login.ui.absenceLate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.MissingRequest
import hu.ocist.enaplo.login.data.responses.MissingResponse
import hu.ocist.enaplo.login.databinding.FragmentWithButtonBinding
import hu.ocist.enaplo.login.ui.*
import java.time.format.DateTimeFormatter

class AbsenceFragment : Fragment(R.layout.fragment_with_button) {

    private lateinit var binding: FragmentWithButtonBinding

    private lateinit var viewModel: TeacherViewModel
    private lateinit var adapter: AbsenceRecyclerViewAdapter
    private lateinit var missing: MutableList<MissingResponse>
    private var lessonId = 0
    private var dividendId = 0
    private var date = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWithButtonBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.missingResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapter.empty()
                        binding.textMessage.visibility = View.VISIBLE
                    } else {
                        missing = it.value as MutableList<MissingResponse>
                        adapter.update(it.value)
                        binding.textMessage.visibility = View.INVISIBLE
                    }
                }
                is Resource.Failure -> {
                    adapter.empty()
                    handleApiError(it) { viewModel.getMissingStudents(lessonId, dividendId) }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.postMissingResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // kiírni és back
                        getView()?.snackbar(it.value.value)
                        (requireActivity() as HomeActivity).popBackStackNavController()
                    }
                }
                is Resource.Failure -> { handleApiError(it) {} }
                is Resource.Loading -> {}
            }
        })

        lessonId = (requireActivity() as HomeActivity).lessonId
        dividendId = (requireActivity() as HomeActivity).dividendId
        date = (requireActivity() as HomeActivity).date

        viewModel.getMissingStudents(lessonId, dividendId)

        initRecyclerView()

        binding.button.setOnClickListener {
            viewModel.postMissingStudents(
                MissingRequest(lessonId, dividendId, date, missing)
            )
        }
        (activity as HomeActivity).setTitle(requireContext().getString(R.string.absences))
    }

    fun setAbsence(id: Int, absent: Boolean) {
        for(item in missing) {
            if (item.int == id) {
                item.bool = absent
                break
            }
        }
    }

    private fun initRecyclerView() {
        adapter = AbsenceRecyclerViewAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}