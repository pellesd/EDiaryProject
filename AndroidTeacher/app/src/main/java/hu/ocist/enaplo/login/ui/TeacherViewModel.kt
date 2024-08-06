package hu.ocist.enaplo.login.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import hu.ocist.enaplo.login.data.network.Resource
import hu.ocist.enaplo.login.data.repository.TeacherRepository
import hu.ocist.enaplo.login.data.requests.*
import hu.ocist.enaplo.login.data.responses.*
import kotlinx.coroutines.launch

class TeacherViewModel(
    private val repository: TeacherRepository
) : ViewModel() {

    // canteen
    private val _canteenResponse = MutableLiveData<Resource<List<CanteenResponse>>>()
    val canteenResponse: LiveData<Resource<List<CanteenResponse>>>
        get() = _canteenResponse

    fun canteen(date: String) = viewModelScope.launch {
       _canteenResponse.value = Resource.Loading
       _canteenResponse.value = repository.canteen(date)
    }

    // canteen
    private val _postCanteenResponse = MutableLiveData<Resource<CanteenResponse>>()
    val postCanteenResponse: LiveData<Resource<CanteenResponse>>
        get() = _postCanteenResponse

    fun postCanteen(req: CanteenRequest) = viewModelScope.launch {
        _postCanteenResponse.value = Resource.Loading
        _postCanteenResponse.value = repository.postCanteen(req)
    }

    // canteen
    private val _deleteCanteenResponse = MutableLiveData<Resource<StringResponse>>()
    val deleteCanteenResponse: LiveData<Resource<StringResponse>>
        get() = _deleteCanteenResponse

    fun deleteCanteen(id: Int) = viewModelScope.launch {
        _deleteCanteenResponse.value = Resource.Loading
        _deleteCanteenResponse.value = repository.deleteCanteen(id)
    }

    // timetable
    private val _timetableResponse = MutableLiveData<Resource<List<TimetableResponse>>>()
    val timetableResponse: LiveData<Resource<List<TimetableResponse>>>
        get() = _timetableResponse

    fun timetable(date: String) = viewModelScope.launch {
        _timetableResponse.value = Resource.Loading
        _timetableResponse.value = repository.timetable(date)
    }

    // inboxMessages
    private val _inboxMessagesResponse = MutableLiveData<Resource<List<MessageResponse>>>()
    val inboxMessagesResponse: LiveData<Resource<List<MessageResponse>>>
        get() = _inboxMessagesResponse

    fun inboxMessages() = viewModelScope.launch {
        _inboxMessagesResponse.value = Resource.Loading
        _inboxMessagesResponse.value = repository.inboxMessages()
    }

    // message
    private val _messageResponse = MutableLiveData<Resource<MessageTeacherResponse>>()
    val messageResponse: LiveData<Resource<MessageTeacherResponse>>
        get() = _messageResponse

    fun message(messageId: Int) = viewModelScope.launch {
        _messageResponse.value = Resource.Loading
        _messageResponse.value = repository.message(messageId)
    }

    // outboxMessages
    private val _outboxMessagesResponse = MutableLiveData<Resource<List<MessageResponse>>>()
    val outboxMessagesResponse: LiveData<Resource<List<MessageResponse>>>
        get() = _outboxMessagesResponse

    fun outboxMessages() = viewModelScope.launch {
        _outboxMessagesResponse.value = Resource.Loading
        _outboxMessagesResponse.value = repository.outboxMessages()
    }

    // messagesToGroup
    private val _messagesToGroupResponse = MutableLiveData<Resource<List<MessageGroupResponse>>>()
    val messagesToGroupResponse: LiveData<Resource<List<MessageGroupResponse>>>
        get() = _messagesToGroupResponse

    fun messagesSentToGroup() = viewModelScope.launch {
        _messagesToGroupResponse.value = Resource.Loading
        _messagesToGroupResponse.value = repository.messagesSentToGroup()
    }

    // groupMembers
    private val _groupMessageMembersResponse = MutableLiveData<Resource<List<String>>>()
    val groupMembersResponse: LiveData<Resource<List<String>>>
        get() = _groupMessageMembersResponse

    fun groupMessageMembers(messageId :Int) = viewModelScope.launch {
        _groupMessageMembersResponse.value = Resource.Loading
        _groupMessageMembersResponse.value = repository.groupMessageMembers(messageId)
    }

    // propitiousTypes
    private val _propitiousTypes = MutableLiveData<Resource<List<IntStringResponse>>>()
    val propitiousTypes: LiveData<Resource<List<IntStringResponse>>>
        get() = _propitiousTypes

    fun propitiousTypes() = viewModelScope.launch {
        _propitiousTypes.value = Resource.Loading
        _propitiousTypes.value = repository.propitiousTypes()
    }

    // admonitoryTypes
    private val _admonitoryTypes = MutableLiveData<Resource<List<IntStringResponse>>>()
    val admonitoryTypes: LiveData<Resource<List<IntStringResponse>>>
        get() = _admonitoryTypes

    fun admonitoryTypes() = viewModelScope.launch {
        _admonitoryTypes.value = Resource.Loading
        _admonitoryTypes.value = repository.admonitoryTypes()
    }

    // taughtGroups
    private val _taughtGroups = MutableLiveData<Resource<List<IntStringResponse>>>()
    val taughtGroupsResponse: LiveData<Resource<List<IntStringResponse>>>
        get() = _taughtGroups

    fun taughtGroups() = viewModelScope.launch {
        _taughtGroups.value = Resource.Loading
        _taughtGroups.value = repository.taughtGroups()
    }

    // taughtGroupMembers
    private val _taughtGroupMembersResponse = MutableLiveData<Resource<List<IntStringResponse>>>()
    val taughtGroupMembersResponse: LiveData<Resource<List<IntStringResponse>>>
        get() = _taughtGroupMembersResponse

    fun groupMembers(id: Int) = viewModelScope.launch {
        _taughtGroupMembersResponse.value = Resource.Loading
        _taughtGroupMembersResponse.value = repository.groupMembers(id)
    }

    // propitiousToStudent
    private val _propitiousResponse = SingleLiveEvent<Resource<StringResponse>>()
    val propitiousResponse: LiveData<Resource<StringResponse>>
        get() = _propitiousResponse

    fun propitiousToStudent(newJudgementRequest: NewJudgementRequest) = viewModelScope.launch {
        _propitiousResponse.value = Resource.Loading
        _propitiousResponse.value = repository.propitiousToStudent(newJudgementRequest)
    }

    // admonitoryToStudent
    private val _admonitoryResponse = SingleLiveEvent<Resource<StringResponse>>()
    val admonitoryResponse: LiveData<Resource<StringResponse>>
        get() = _admonitoryResponse

    fun admonitoryToStudent(newJudgementRequest: NewJudgementRequest) = viewModelScope.launch {
        _admonitoryResponse.value = Resource.Loading
        _admonitoryResponse.value = repository.admonitoryToStudent(newJudgementRequest)
    }

    // sendMessageToGroup
    private val _sendMessageToGroupResponse = SingleLiveEvent<Resource<StringResponse>>()
    val sendMessageToGroupResponse: LiveData<Resource<StringResponse>>
        get() = _sendMessageToGroupResponse

    fun sendMessageToGroup(messageToGroupRequest: MessageToGroupRequest) = viewModelScope.launch {
        _sendMessageToGroupResponse.value = Resource.Loading
        _sendMessageToGroupResponse.value = repository.sendMessageToGroup(messageToGroupRequest)
    }

    // sendMessageToTeacher
    private val _sendMessageToTeacherResponse = SingleLiveEvent<Resource<StringResponse>>()
    val sendMessageToTeacherResponse: LiveData<Resource<StringResponse>>
        get() = _sendMessageToTeacherResponse

    fun sendMessageToTeacher(value: PostMessageTeacherRequest) = viewModelScope.launch{
        _sendMessageToTeacherResponse.value = Resource.Loading
        _sendMessageToTeacherResponse.value = repository.sendMessageToTeacher(value)
    }

    // teachers
    private val _teachersResponse = MutableLiveData<Resource<List<IntStringResponse>>>()
    val teachersResponse: LiveData<Resource<List<IntStringResponse>>>
        get() = _teachersResponse

    fun teachers() = viewModelScope.launch{
        _teachersResponse.value = Resource.Loading
        _teachersResponse.value = repository.teachers()
    }

    // getRegisterLessonRequest
    private val _getRegisterLessonResponse = MutableLiveData<Resource<RegisterLessonResponse>>()
    val getRegisterLessonResponse: LiveData<Resource<RegisterLessonResponse>>
        get() = _getRegisterLessonResponse

    fun getRegisterLesson(keys: LessonKeysRequest) = viewModelScope.launch{
        _getRegisterLessonResponse.value = Resource.Loading
        _getRegisterLessonResponse.value = repository.getRegisterLesson(keys)
    }

    // postRegisterLessonResponse
    private val _postRegisterLessonResponse = SingleLiveEvent<Resource<StringResponse>>()
    val postRegisterLessonResponse: LiveData<Resource<StringResponse>>
        get() = _postRegisterLessonResponse

    fun postRegisterLesson(registerLessonRequest: RegisterLessonRequest) = viewModelScope.launch{
        _postRegisterLessonResponse.value = Resource.Loading
        _postRegisterLessonResponse.value = repository.postRegisterLesson(registerLessonRequest)
    }

    // MissingRequest
    private val _missingResponse = MutableLiveData<Resource<List<MissingResponse>>>()
    val missingResponse: LiveData<Resource<List<MissingResponse>>>
        get() = _missingResponse

    fun getMissingStudents(lessonId: Int, dividendId: Int) = viewModelScope.launch{
        _missingResponse.value = Resource.Loading
        _missingResponse.value = repository.getMissingStudents(lessonId, dividendId)
    }

    // PostMissingResponse
    private val _postMissingResponse = SingleLiveEvent<Resource<StringResponse>>()
    val postMissingResponse: LiveData<Resource<StringResponse>>
        get() = _postMissingResponse

    fun postMissingStudents(missingRequest: MissingRequest) = viewModelScope.launch{
        _postMissingResponse.value = Resource.Loading
        _postMissingResponse.value = repository.postMissingStudents(missingRequest)
    }

    // LateRequest
    private val _lateResponse = MutableLiveData<Resource<List<LateResponse>>>()
    val lateResponse: LiveData<Resource<List<LateResponse>>>
        get() = _lateResponse

    fun getLate(lessonId: Int, dividendId: Int) = viewModelScope.launch{
        _lateResponse.value = Resource.Loading
        _lateResponse.value = repository.getLate(lessonId, dividendId)
    }

    // LateResponse
    private val _postLateResponse = SingleLiveEvent<Resource<StringResponse>>()
    val postLateResponse: LiveData<Resource<StringResponse>>
        get() = _postLateResponse

    fun postLate(lateRequest: LateRequest) = viewModelScope.launch{
        _postLateResponse.value = Resource.Loading
        _postLateResponse.value = repository.postLate(lateRequest)
    }

    // Name
    private val _nameResponse = MutableLiveData<Resource<StringResponse>>()
    val nameResponse: LiveData<Resource<StringResponse>>
        get() = _nameResponse

    fun name() = viewModelScope.launch{
        _nameResponse.value = Resource.Loading
        _nameResponse.value = repository.name()
    }

    // GradeTypes
    private val _gradeTypesResponse = MutableLiveData<Resource<List<GradeTypeResponse>>>()
    val gradeTypesResponse: LiveData<Resource<List<GradeTypeResponse>>>
        get() = _gradeTypesResponse

    fun gradeTypes() = viewModelScope.launch{
        _gradeTypesResponse.value = Resource.Loading
        _gradeTypesResponse.value = repository.gradeTypes()
    }

    // SendGrade
    private val _sendGradeResponse = SingleLiveEvent<Resource<StringResponse>>()
    val sendGradeResponse: LiveData<Resource<StringResponse>>
        get() = _sendGradeResponse

    fun sendGrade(grade: AddGradeRequest) = viewModelScope.launch{
        _sendGradeResponse.value = Resource.Loading
        _sendGradeResponse.value = repository.sendGrade(grade)
    }

    // SendGrades
    private val _sendGradesResponse = SingleLiveEvent<Resource<StringResponse>>()
    val sendGradesResponse: LiveData<Resource<StringResponse>>
        get() = _sendGradesResponse

    fun sendGrades(grades: List<AddGradeRequest>) = viewModelScope.launch{
        _sendGradesResponse.value = Resource.Loading
        _sendGradesResponse.value = repository.sendGrades(grades)
    }

    // getMyClass
    private val _getMyClassResponse = MutableLiveData<Resource<MyClassResponse>>()
    val getMyClassResponse: LiveData<Resource<MyClassResponse>>
        get() = _getMyClassResponse

    fun getMyClass() = viewModelScope.launch{
        _getMyClassResponse.value = Resource.Loading
        _getMyClassResponse.value = repository.getMyClass()
    }

    // postMyClass
    private val _postMyClassResponse = SingleLiveEvent<Resource<StringResponse>>()
    val postMyClassResponse: LiveData<Resource<StringResponse>>
        get() = _postMyClassResponse

    fun postMyClass(myClassResponse: MyClassResponse) = viewModelScope.launch{
        _postMyClassResponse.value = Resource.Loading
        _postMyClassResponse.value = repository.postMyClass(myClassResponse)
    }

    // propitiouses
    private val _propitiousesResponse = MutableLiveData<Resource<List<JudgementResponse>>>()
    val propitiousesResponse: LiveData<Resource<List<JudgementResponse>>>
        get() = _propitiousesResponse

    fun propitiouses() = viewModelScope.launch{
        _propitiousesResponse.value = Resource.Loading
        _propitiousesResponse.value = repository.propitiouses()
    }

    // admonitories
    private val _admonitoriesResponse = MutableLiveData<Resource<List<JudgementResponse>>>()
    val admonitoriesResponse: LiveData<Resource<List<JudgementResponse>>>
        get() = _admonitoriesResponse

    fun admonitories() = viewModelScope.launch{
        _admonitoriesResponse.value = Resource.Loading
        _admonitoriesResponse.value = repository.admonitories()
    }

    // subjects
    private val _subjectsResponse = MutableLiveData<Resource<List<IntStringResponse>>>()
    val subjectsResponse: LiveData<Resource<List<IntStringResponse>>>
        get() = _subjectsResponse

    fun subjects(groupId: Int) = viewModelScope.launch{
        _subjectsResponse.value = Resource.Loading
        _subjectsResponse.value = repository.subjects(groupId)
    }

    // subjects
    private val _sumGradesResponse = MutableLiveData<Resource<List<SumGradesResponse>>>()
    val sumGradesResponse: LiveData<Resource<List<SumGradesResponse>>>
        get() = _sumGradesResponse

    fun sumGrades(dividendId: Int) = viewModelScope.launch{
        _sumGradesResponse.value = Resource.Loading
        _sumGradesResponse.value = repository.sumGrades(dividendId)
    }

    // grades
    private val _gradesResponse = MutableLiveData<Resource<List<GradesResponse>>>()
    val gradesResponse: LiveData<Resource<List<GradesResponse>>>
        get() = _gradesResponse

    fun grades(grade: GradesRequest) = viewModelScope.launch {
        _gradesResponse.value = Resource.Loading
        _gradesResponse.value = repository.grades(grade)
    }
}