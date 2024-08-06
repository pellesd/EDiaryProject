package hu.ocist.enaplo.login.ui.myClass

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.responses.MyClassResponse
import hu.ocist.enaplo.login.databinding.FragmentMyClassBinding
import hu.ocist.enaplo.login.ui.StudentViewModel
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.visible
import hu.ocist.enaplo.login.ui.HomeActivity

class MyClassFragment : Fragment(R.layout.fragment_my_class) {

    private lateinit var binding: FragmentMyClassBinding

    private lateinit var viewModel:  StudentViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMyClassBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.myClass.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    bind_data(it.value)
                }
                is Resource.Failure -> {
                    unbind_data()
                    handleApiError(it) { viewModel.myClass() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.myClass()
    }

    private fun unbind_data() {
        binding.textMessage.visible(true)
        binding.layout.visible(false)

    }

    private fun bind_data(myClass: MyClassResponse) {
        binding.textMessage.visible(false)
        binding.name.text = myClass.name
        binding.headerTeacher.text = myClass.headTeacher
        binding.subHeaderTeacher.text = myClass.subHeadTeacher
        binding.seven1.text = myClass.seven1 ?: getString(R.string.no_data)
        binding.seven2.text = myClass.seven2 ?: getString(R.string.no_data)
        binding.layout.visible(true)
    }
}