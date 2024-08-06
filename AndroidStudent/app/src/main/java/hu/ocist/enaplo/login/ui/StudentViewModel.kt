package hu.ocist.enaplo.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.repository.StudentRepository
import hu.ocist.enaplo.login.data.requests.GradesRequest
import hu.ocist.enaplo.login.data.requests.StringRequest
import hu.ocist.enaplo.login.data.responses.*
import kotlinx.coroutines.launch

class StudentViewModel(
    private val repository: StudentRepository
) : ViewModel() {

    // canteen
    private val _canteenResponse : MutableLiveData<Resource<List<CanteenResponse>>> = MutableLiveData()
    val canteenResponse: LiveData<Resource<List<CanteenResponse>>>
        get() = _canteenResponse

    fun canteen(date: String) = viewModelScope.launch {
       _canteenResponse.value = Resource.Loading
       _canteenResponse.value = repository.canteen(date)
    }

    // absence
    private val _absenceResponse : MutableLiveData<Resource<List<AbsenceResponse>>> = MutableLiveData()
    val absenceResponse: LiveData<Resource<List<AbsenceResponse>>>
        get() = _absenceResponse

    fun absences() = viewModelScope.launch {
        _absenceResponse.value = Resource.Loading
        _absenceResponse.value = repository.absences()
    }

    // message
    private val _messageResponse : MutableLiveData<Resource<List<MessageResponse>>> = MutableLiveData()
    val messageResponse: LiveData<Resource<List<MessageResponse>>>
        get() = _messageResponse

    fun messages() = viewModelScope.launch {
        _messageResponse.value = Resource.Loading
        _messageResponse.value = repository.messages()
    }

    // admonitory
    private val _admonitoryResponse : MutableLiveData<Resource<List<JudgementResponse>>> = MutableLiveData()
    val admonitoryResponse: LiveData<Resource<List<JudgementResponse>>>
        get() = _admonitoryResponse

    fun admonitory() = viewModelScope.launch {
        _admonitoryResponse.value = Resource.Loading
        _admonitoryResponse.value = repository.admonitory()
    }

    // propitious
    private val _propitiousResponse : MutableLiveData<Resource<List<JudgementResponse>>> = MutableLiveData()
    val propitiousResponse: LiveData<Resource<List<JudgementResponse>>>
        get() = _propitiousResponse

    fun propitious() = viewModelScope.launch {
        _propitiousResponse.value = Resource.Loading
        _propitiousResponse.value = repository.propitious()
    }

    // late
    private val _lateResponse : MutableLiveData<Resource<List<LateResponse>>> = MutableLiveData()
    val lateResponse: LiveData<Resource<List<LateResponse>>>
        get() = _lateResponse

    fun late() = viewModelScope.launch {
        _lateResponse.value = Resource.Loading
        _lateResponse.value = repository.late()
    }

    // myClass
    private val _myClass : MutableLiveData<Resource<MyClassResponse>> = MutableLiveData()
    val myClass: LiveData<Resource<MyClassResponse>>
        get() = _myClass

    fun myClass() = viewModelScope.launch {
        _myClass.value = Resource.Loading
        _myClass.value = repository.myClass()
    }

    // late
    private val _examsResponse : MutableLiveData<Resource<List<ExamResponse>>> = MutableLiveData()
    val examsResponse: LiveData<Resource<List<ExamResponse>>>
        get() = _examsResponse

    fun exams() = viewModelScope.launch {
        _examsResponse.value = Resource.Loading
        _examsResponse.value = repository.exams()
    }

    // late
    private val _sumGradesResponse : MutableLiveData<Resource<List<SumGradesResponse>>> = MutableLiveData()
    val sumGradesResponse: LiveData<Resource<List<SumGradesResponse>>>
        get() = _sumGradesResponse

    fun sumGrades() = viewModelScope.launch {
        _sumGradesResponse.value = Resource.Loading
        _sumGradesResponse.value = repository.sumGrades()
    }

    // grades
    private val _gradesResponse : MutableLiveData<Resource<List<GradesResponse>>> = MutableLiveData()
    val gradesResponse: LiveData<Resource<List<GradesResponse>>>
        get() = _gradesResponse

    fun grades(grade: GradesRequest) = viewModelScope.launch {
        _gradesResponse.value = Resource.Loading
        _gradesResponse.value = repository.grades(grade)
    }

    // groupMembers
    private val _groupMembersResponse : MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val groupMembersResponse: LiveData<Resource<List<String>>>
        get() = _groupMembersResponse

    fun groupMembers(subject: String) = viewModelScope.launch {
        _groupMembersResponse.value = Resource.Loading
        _groupMembersResponse.value = repository.groupMembers(subject)
    }

    // timetable
    private val _timetableResponse : MutableLiveData<Resource<List<TimetableResponse>>> = MutableLiveData()
    val timetableResponse: LiveData<Resource<List<TimetableResponse>>>
        get() = _timetableResponse

    fun timetable(date: String) = viewModelScope.launch {
        _timetableResponse.value = Resource.Loading
        _timetableResponse.value = repository.timetable(date)
    }

    // Name
    private val _nameResponse : MutableLiveData<Resource<StringResponse>> = MutableLiveData()
    val nameResponse: LiveData<Resource<StringResponse>>
        get() = _nameResponse

    fun name() = viewModelScope.launch{
        _nameResponse.value = Resource.Loading
        _nameResponse.value = repository.name()
    }
}