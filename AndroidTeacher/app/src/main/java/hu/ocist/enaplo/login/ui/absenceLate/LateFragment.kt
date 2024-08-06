package hu.ocist.enaplo.login.ui.absenceLate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.LateRequest
import hu.ocist.enaplo.login.data.responses.LateResponse
import hu.ocist.enaplo.login.databinding.FragmentWithButtonBinding
import hu.ocist.enaplo.login.ui.*

class LateFragment : Fragment(R.layout.fragment_with_button) {

    private lateinit var binding: FragmentWithButtonBinding

    private lateinit var viewModel: TeacherViewModel
    private lateinit var adapter: LateRecyclerViewAdapter
    private lateinit var lates: MutableList<LateResponse>
    private var lessonId = 0
    private var dividendId = 0
    private var date = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWithButtonBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.lateResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        adapter.empty()
                        binding.textMessage.visibility = View.VISIBLE
                    } else {
                        lates = it.value as MutableList<LateResponse>
                        adapter.update(it.value)
                        binding.textMessage.visibility = View.INVISIBLE
                    }
                }
                is Resource.Failure -> {
                    adapter.empty()
                    handleApiError(it) { viewModel.getLate(lessonId, dividendId) }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.postLateResponse.observe(viewLifecycleOwner, Observer {
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

        viewModel.getLate(lessonId, dividendId)

        initRecyclerView()

        binding.button.setOnClickListener {
            viewModel.postLate(
                LateRequest(lessonId, dividendId, date, lates)
            )
        }
        (activity as HomeActivity).setTitle(requireContext().getString(R.string.late))
    }

    fun setLate(id: Int, len: Int?) {
        for(item in lates) {
            if (item.int == id) {
                item.len = len
                break
            }
        }
    }

    private fun initRecyclerView() {
        adapter = LateRecyclerViewAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}