package hu.ocist.enaplo.login.data.repository

import hu.ocist.enaplo.login.data.network.TeacherApi
import hu.ocist.enaplo.login.data.requests.*
import hu.ocist.enaplo.login.data.responses.MyClassResponse

class TeacherRepository(
    private val api: TeacherApi
) : SafeApiCall {

    suspend fun canteen(date: String) = safeApiCall {
        api.canteen(date)
    }

    suspend fun postCanteen(postCanteenResponse: CanteenRequest) = safeApiCall {
        api.postCanteen(postCanteenResponse)
    }

    suspend fun deleteCanteen(id: Int) = safeApiCall {
        api.deleteCanteen(id)
    }

    suspend fun timetable(date: String) = safeApiCall {
        api.timetable(date)
    }

    suspend fun inboxMessages() = safeApiCall {
        api.messages()
    }

    suspend fun message(messageId: Int) = safeApiCall {
        api.message(messageId)
    }

    suspend fun outboxMessages() = safeApiCall {
        api.outboxMessages()
    }

    suspend fun messagesSentToGroup() = safeApiCall {
        api.messagesSentToGroup()
    }

    suspend fun groupMessageMembers(messageId: Int) = safeApiCall {
        api.groupMessageMembers(messageId)
    }

    suspend fun propitiousTypes() = safeApiCall {
        api.propitiousTypes()
    }

    suspend fun admonitoryTypes() = safeApiCall {
        api.admonitoryTypes()
    }

    suspend fun taughtGroups() = safeApiCall {
        api.taughtGroups()
    }

    suspend fun groupMembers(id: Int) = safeApiCall {
        api.groupMembers(id)
    }

    suspend fun propitiousToStudent(newJudgementRequest: NewJudgementRequest) = safeApiCall {
        api.propitiousToStudent(newJudgementRequest)
    }

    suspend fun admonitoryToStudent(newJudgementRequest: NewJudgementRequest) = safeApiCall {
        api.admonitoryToStudent(newJudgementRequest)
    }

    suspend fun sendMessageToGroup(messageToGroupRequest: MessageToGroupRequest) = safeApiCall {
        api.sendMessageToGroup(messageToGroupRequest)
    }

    suspend fun sendMessageToTeacher(value: PostMessageTeacherRequest) = safeApiCall {
        api.sendMessageToTeacher(value)
    }

    suspend fun teachers() = safeApiCall {
        api.teachers()
    }

    suspend fun getRegisterLesson(
        keys: LessonKeysRequest
    ) = safeApiCall {
        api.getRegisterLesson(keys.date, keys.numberOfLesson, keys.dividendId)
    }

    suspend fun postRegisterLesson(registerLessonRequest: RegisterLessonRequest) = safeApiCall {
        api.postRegisterLesson(registerLessonRequest)
    }

    suspend fun getMissingStudents(lessonId: Int, dividendId: Int) = safeApiCall {
        api.getMissingStudents(lessonId, dividendId)
    }

    suspend fun postMissingStudents(missingRequest: MissingRequest) = safeApiCall {
        api.postMissingStudents(missingRequest)
    }

    suspend fun getLate(lessonId: Int, dividendId: Int) = safeApiCall {
        api.getLate(lessonId, dividendId)
    }

    suspend fun postLate(lateRequest: LateRequest) = safeApiCall {
        api.postLate(lateRequest)
    }

    suspend fun name() = safeApiCall {
        api.name()
    }

    suspend fun gradeTypes() = safeApiCall {
        api.gradeTypes()
    }

    suspend fun sendGrade(grade: AddGradeRequest) = safeApiCall {
        api.sendGrade(grade)
    }

    suspend fun sendGrades(grades: List<AddGradeRequest>) = safeApiCall {
        api.sendGrades(grades)
    }

    suspend fun getMyClass() = safeApiCall {
        api.getMyClass()
    }

    suspend fun postMyClass(myClassResponse: MyClassResponse) = safeApiCall {
        api.postMyClass(myClassResponse)
    }

    suspend fun propitiouses() = safeApiCall {
        api.propitiouses()
    }

    suspend fun admonitories() = safeApiCall {
        api.admonitories()
    }

    suspend fun subjects(groupId: Int) = safeApiCall {
        api.subjects(groupId)
    }

    suspend fun sumGrades(dividendId: Int) = safeApiCall {
        api.sumGrades(dividendId)
    }

    suspend fun grades(grade: GradesRequest) = safeApiCall {
        api.grades(grade.semester, grade.subject, grade.studentId)
    }
}