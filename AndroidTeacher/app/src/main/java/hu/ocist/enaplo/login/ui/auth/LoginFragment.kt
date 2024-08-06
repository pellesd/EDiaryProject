package hu.ocist.enaplo.login.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.UserPreferences
import hu.ocist.enaplo.login.data.network.RemoteDataSource
import hu.ocist.enaplo.login.databinding.FragmentLoginBinding
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.network.TeacherAuthApi
import hu.ocist.enaplo.login.data.repository.TeacherAuthRepository
import hu.ocist.enaplo.login.data.requests.LoginRequest
import hu.ocist.enaplo.login.ui.enable
import hu.ocist.enaplo.login.ui.handleApiError
import hu.ocist.enaplo.login.ui.HomeActivity
import hu.ocist.enaplo.login.ui.startNewActivity
import hu.ocist.enaplo.login.ui.visible
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userPreferences: UserPreferences
    private val remoteDataSource = RemoteDataSource()
    private lateinit var viewModel: TeacherAuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        binding.loginButton.enable(false)

        userPreferences = UserPreferences(requireContext())
        lifecycleScope.launch {
            userPreferences.jwtToken.first()
        }

        viewModel = TeacherAuthViewModel(TeacherAuthRepository(remoteDataSource.buildApi(TeacherAuthApi::class.java), userPreferences))

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    lifecycleScope.launch {
                        viewModel.saveJwtToken(it.value.value)
                    }
                    requireActivity().startNewActivity(HomeActivity::class.java)
                }
                is Resource.Failure -> handleApiError(it) { login() }
                is Resource.Loading -> {}
            }
        })

        binding.password.addTextChangedListener {
            val username = binding.username.text.toString().trim()
            binding.loginButton.enable(it.toString().isNotEmpty() && username.isNotEmpty())
        }

        binding.loginButton.setOnClickListener {
            login()
            (requireActivity() as TeacherAuthActivity).hideKeyboard(requireView())
        }
    }

    private fun login() {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()
        viewModel.login(LoginRequest(username, password))
    }
}