package hu.ocist.enaplo.login.ui.message

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.responses.MessageTeacherResponse
import hu.ocist.enaplo.login.databinding.FragmentMessageTeacherBinding
import hu.ocist.enaplo.login.ui.*

class MessageTeacherFragment : Fragment(R.layout.fragment_message_teacher) {

    private lateinit var binding: FragmentMessageTeacherBinding

    private lateinit var viewModel:  TeacherViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMessageTeacherBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.messageResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    bindData(it.value)
                }
                is Resource.Failure -> {
                    unbindData()
                    handleApiError(it) { getMessage() }
                }
                is Resource.Loading -> {}
            }
        })

        getMessage()
    }

    private fun unbindData() {
        binding.textMessage.visible(true)
        binding.layout.visible(false)

    }

    private fun getMessage() {
        val messageId = (activity as HomeActivity).messageId
        viewModel.message(messageId)
    }

    private fun bindData(message: MessageTeacherResponse) {
        binding.textMessage.visible(false)
        binding.date.text = stringToDateFormat(message.date)
        binding.from.text = message.from
        binding.to.text = message.to
        binding.message.text = message.message
        binding.layout.visible(true)
    }
}