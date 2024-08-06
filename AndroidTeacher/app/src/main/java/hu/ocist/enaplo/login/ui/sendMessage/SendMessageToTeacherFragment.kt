package hu.ocist.enaplo.login.ui.sendMessage

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
import hu.ocist.enaplo.login.data.requests.IntStringRequest
import hu.ocist.enaplo.login.data.requests.PostMessageTeacherRequest
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.databinding.FragmentSendMessageToTeacherBinding
import hu.ocist.enaplo.login.ui.*
import java.util.*


class SendMessageToTeacherFragment : Fragment(R.layout.fragment_send_message_to_teacher) {

    private lateinit var binding: FragmentSendMessageToTeacherBinding
    private lateinit var viewModel:  TeacherViewModel

    // Teachers
    private lateinit var teachers: List<IntStringResponse>
    private var teachersSpinner: Spinner? = null
    private var teacherId = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (requireActivity() as HomeActivity).viewModel

        binding = FragmentSendMessageToTeacherBinding.bind(view)

        viewModel.teachersResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            binding.submit.enable(it is Resource.Success)
            when(it) {
                is Resource.Success -> {
                    if(it.value.isEmpty()) {
                        getView()?.snackbar("Hiba történt! Kérlek most ne rögzítsd!")
                    } else {
                        // feltölteni spinnert az opciókkal
                        fillTeachersSpinner(it.value)
                    }
                }
                is Resource.Failure -> {
                    handleApiError(it) { viewModel.teachers() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.sendMessageToTeacherResponse.observe(viewLifecycleOwner, Observer {
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

        viewModel.teachers()


        binding.editTextTextMultiLine.addTextChangedListener {
            binding.submit.enable(it.toString().isNotEmpty())
        }


        binding.submit.setOnClickListener {
            val text = binding.editTextTextMultiLine.text.toString()
            viewModel.sendMessageToTeacher(
                PostMessageTeacherRequest(
                    teacherId, text
                )
            )
        }
    }


    private fun fillTeachersSpinner(_teachers: List<IntStringResponse>) {
        teachers = _teachers

        val options = ArrayList<String>()
        for (teacher in teachers)
            options.add(teacher.string.trim())

        teachersSpinner = requireActivity().findViewById(R.id.teacher_spinner)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        teachersSpinner?.adapter = adapter
        teachersSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                teacherId = getIntByList(teachers, item)
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