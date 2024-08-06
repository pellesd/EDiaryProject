package hu.ocist.enaplo.login.ui.sendMessage

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.requests.MessageToGroupRequest
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.databinding.FragmentSendMessageToGroupBinding
import hu.ocist.enaplo.login.ui.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class SendMessageToGroupFragment : Fragment(R.layout.fragment_send_message_to_group) {

    private lateinit var binding: FragmentSendMessageToGroupBinding
    private lateinit var viewModel:  TeacherViewModel

    // TaughtGroups
    private lateinit var taughtGroups: List<IntStringResponse>
    private var groupsSpinner: Spinner? = null
    private var groupId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentSendMessageToGroupBinding.bind(view)

        viewModel.taughtGroupsResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // feltölteni spinnert az opciókkal
                        fillGroupsSpinner(it.value)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.taughtGroups() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.sendMessageToGroupResponse.observe(viewLifecycleOwner, Observer {
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

        viewModel.taughtGroups()

        val localDate = LocalDate.now()
            .plusDays(10)
            .format(DateTimeFormatter.ISO_LOCAL_DATE)
            .toString()
            .replace("-", ".")

        binding.datePicker.text = localDate

        binding.editTextTextMultiLine.addTextChangedListener {
            binding.submit.enable(it.toString().isNotEmpty())
        }


        binding.datePicker.setOnClickListener {
            dataPickerListener()
        }

        binding.edit.setOnClickListener {
            dataPickerListener()
        }

        binding.submit.setOnClickListener {
            val text = binding.editTextTextMultiLine.text.toString()
            val dateString = binding.datePicker.text.toString().replace(".", "-")
            viewModel.sendMessageToGroup(
                MessageToGroupRequest(
                    groupId, text, "${dateString}T23:59:59.999Z"
                )
            )
        }
    }

    private fun dataPickerListener() {
        var ld = binding.datePicker.text.split(".")

        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                var sMonth = (mMonth + 1).toString()
                if (sMonth.length < 2)
                    sMonth = "0$sMonth"
                var sDay= mDay.toString()
                if (sDay.length < 2)
                    sDay = "0$sDay"
                binding.datePicker.text = "${mYear}.${sMonth}.$sDay"
            }, ld[0].toInt(), ld[1].toInt() - 1, ld[2].toInt())
        // show
        dpd.show()
    }


    private fun fillGroupsSpinner(groups: List<IntStringResponse>) {
        taughtGroups = groups

        val options = ArrayList<String>()
        for (taughtGroup in taughtGroups)
            options.add(taughtGroup.string.trim())

        groupsSpinner = requireActivity().findViewById(R.id.groups_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        groupsSpinner?.adapter = adapter
        groupsSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                groupId = getIntByList(taughtGroups, item)
            }
        }
    }

    private fun getIntByList(list: List<IntStringResponse>, string: String): Int {
        var id = -1
        for (type in list) {
            if (type.string.trim() == string) {
                id = type.int
                break
            }
        }
        return id
    }
}