package hu.ocist.enaplo.login.ui.myClass

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import hu.ocist.enaplo.login.R
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.responses.IntStringResponse
import hu.ocist.enaplo.login.data.responses.MyClassResponse
import hu.ocist.enaplo.login.databinding.FragmentMyClassBinding
import hu.ocist.enaplo.login.ui.*

class MyClassFragment : Fragment(R.layout.fragment_my_class) {

    private lateinit var binding: FragmentMyClassBinding

    private lateinit var viewModel: TeacherViewModel

    private var myClass: MyClassResponse? = null

    // Sevens
    private lateinit var students: MutableList<IntStringResponse>
    private var studentSpinner1: Spinner? = null
    private var studentSpinner2: Spinner? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentMyClassBinding.bind(view)

        viewModel = (requireActivity() as HomeActivity).viewModel

        viewModel.getMyClassResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    myClass = it.value
                    bindData(it.value)
                    viewModel.groupMembers(it.value.groupId)
                }
                is Resource.Failure -> {
                    unbindData()
                    handleApiError(it) { viewModel.getMyClass() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.taughtGroupMembersResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    fillStudentsSpinner(it.value)

                    val activity = (requireActivity() as HomeActivity)
                    if (activity.sevenId1 != -2 && activity.sevenId1 != null) {
                        binding.studentsSpinner1.setSelection(getPosById(students, activity.sevenId1!!))
                    } else {
                        myClass?.sevenId1?.let { sevenId1 ->
                            binding.studentsSpinner1.setSelection(getPosById(students, sevenId1))
                        }
                    }
                    if (activity.sevenId2 != -2 && activity.sevenId2 != null) {
                        binding.studentsSpinner2.setSelection(getPosById(students, activity.sevenId2!!))
                    } else {
                        myClass?.sevenId2?.let { sevenId2 ->
                            binding.studentsSpinner2.setSelection(getPosById(students, sevenId2))
                        }
                    }
                }
                is Resource.Failure -> {
                    unbindData()
                    handleApiError(it) { viewModel.getMyClass() }
                }
                is Resource.Loading -> {}
            }
        })
        viewModel.postMyClassResponse.observe(viewLifecycleOwner, Observer {
            binding.loading.visible(it is Resource.Loading)
            when(it) {
                is Resource.Success -> {
                    // kiírni és back
                    getView()?.snackbar(it.value.value)
                    (requireActivity() as HomeActivity).popBackStackNavController()
                }
                is Resource.Failure -> {
                    unbindData()
                    handleApiError(it) { viewModel.getMyClass() }
                }
                is Resource.Loading -> {}
            }
        })

        viewModel.getMyClass()

        binding.submit.setOnClickListener {
            (requireActivity() as HomeActivity).sevenId1 = myClass?.sevenId1
            (requireActivity() as HomeActivity).sevenId2 = myClass?.sevenId2
            myClass?.let {
                viewModel.postMyClass(it)
            }
        }
    }

    private fun unbindData() {
        binding.textMessage.visible(true)
        binding.constraintLayout.visible(null)
    }

    private fun bindData(myClass: MyClassResponse) {
        binding.textMessage.visible(false)
        binding.name.text = myClass.name
        binding.headerTeacher.text = myClass.headTeacher
        binding.subHeaderTeacher.text = myClass.subHeadTeacher
        binding.constraintLayout.visible(true)
    }

    private fun fillStudentsSpinner(studentsList: List<IntStringResponse>) {
        students = studentsList as MutableList<IntStringResponse>
        if ((requireActivity() as HomeActivity).sevenId1 == -2)
            students.add(0, IntStringResponse(-1, "Nincs hetes"))

        val options = ArrayList<String>()
        for (student in students)
            options.add(student.string.trim())

        studentSpinner1 = requireActivity().findViewById(R.id.students_spinner1)
        val adapter1 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        studentSpinner2 = requireActivity().findViewById(R.id.students_spinner2)
        val adapter2 = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            options
        )

        studentSpinner1?.adapter = adapter1
        studentSpinner1?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                val int = getIntByList(students, item)
                if (int == -1)
                    myClass?.sevenId1 = null
                else
                    myClass?.sevenId1 = int

                val activity = (requireActivity() as HomeActivity)
                if (activity.sevenId1 == -2)
                    activity.sevenId1 = int
            }
        }

        studentSpinner2?.adapter = adapter2
        studentSpinner2?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val item: String = parent?.getItemAtPosition(position).toString()
                val int = getIntByList(students, item)
                if (int == -1)
                    myClass?.sevenId2 = null
                else
                    myClass?.sevenId2 = int

                val activity = (requireActivity() as HomeActivity)
                if (activity.sevenId2 == -2)
                    activity.sevenId2 = int
            }
        }
    }

    private fun getIntByList(list: List<IntStringResponse>, string: String): Int {
        for (type in list) {
            if (type.string.trim() == string) {
                return type.int
            }
        }
        return -1
    }
    private fun getPosById(list: List<IntStringResponse>, id: Int): Int {
        for (i in list.indices) {
            if (list[i].int == id) {
                return i
            }
        }
        return -1
    }
}