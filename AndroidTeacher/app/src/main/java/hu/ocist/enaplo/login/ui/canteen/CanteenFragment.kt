package hu.ocist.enaplo.login.ui.canteen

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.CanteenRequest
import hu.ocist.enaplo.login.databinding.FragmentWithArrowsFabBinding
import hu.ocist.enaplo.login.ui.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CanteenFragment : Fragment(R.layout.fragment_with_arrows_fab) {

    private lateinit var binding: FragmentWithArrowsFabBinding

    private lateinit var viewModel:  TeacherViewModel

    private var localDate = LocalDate.now()
    private lateinit var adapter: CanteenRecyclerViewAdapter

    private var deletePosition: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentWithArrowsFabBinding.bind(view)
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
        viewModel.postCanteenResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    adapter.addOrUpdateItem(it.value)
                }
                is Resource.Failure -> {
                    adapter.empty()
                    handleApiError(it) {  }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.deleteCanteenResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    requireView().snackbar(it.value.value)
                    deletePosition?.let { pos ->
                        adapter.deleteItem(pos)
                    }
                }
                is Resource.Failure -> {
                    adapter.empty()
                    handleApiError(it) { viewModel.deleteCanteen(id) }
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

        binding.buttonToolbar.fab.setOnClickListener {
            callAlertDialog(null, "", "", "")
        }

        initRecyclerView()
    }

    fun callAlertDialog(
        id: Int?,
        firstText: String,
        secondText: String,
        extraText: String,
    ) {

        var dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.add_food_dialog_layout)

        val firstMealInput = dialog.findViewById<EditText>(R.id.firstText)
        val secondMealInput = dialog.findViewById<EditText>(R.id.secondText)
        val extraMealInput = dialog.findViewById<EditText>(R.id.extraText)

        firstMealInput.setText(firstText)
        secondMealInput.setText(secondText)
        extraMealInput.setText(extraText)

        val buttonOk = dialog.findViewById<Button>(R.id.buttonOk)
        buttonOk.setOnClickListener {
            viewModel.postCanteen(CanteenRequest(
                id,
                localDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                firstMealInput.text.toString().trim() // capitalize
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                secondMealInput.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                extraMealInput.text.toString().trim()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            ))
            dialog.dismiss()
        }

        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show();
    }

    fun callDelete(id: Int, position: Int) {
        viewModel.deleteCanteen(id)
        deletePosition = position
    }

    private fun sendCanteen() {
        viewModel.canteen(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        (activity as HomeActivity).setTitle(localDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
    }

    private fun initRecyclerView() {
        adapter = CanteenRecyclerViewAdapter(this)
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
    }
}