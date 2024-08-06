package hu.ocist.enaplo.login.data.repository

import hu.ocist.enaplo.login.data.requests.GradesRequest
import hu.ocist.enaplo.login.data.network.StudentApi

class StudentRepository(
    private val api: StudentApi
) : SafeApiCall {

    suspend fun canteen(date: String) = safeApiCall {
        api.canteen(date)
    }

    suspend fun absences() = safeApiCall {
        api.absences()
    }

    suspend fun messages() = safeApiCall {
        api.messages()
    }

    suspend fun admonitory() = safeApiCall {
        api.admonitory()
    }

    suspend fun propitious() = safeApiCall {
        api.propitious()
    }

    suspend fun late() = safeApiCall {
        api.late()
    }

    suspend fun myClass() = safeApiCall {
        api.myClass()
    }

    suspend fun exams() = safeApiCall {
        api.exams()
    }

    suspend fun sumGrades() = safeApiCall {
        api.sumGrades()
    }

    suspend fun grades(grade: GradesRequest) = safeApiCall {
        api.grades(grade.semester, grade.subject)
    }

    suspend fun groupMembers(group: String) = safeApiCall {
        api.groupMembers(group)
    }

    suspend fun timetable(date: String) = safeApiCall {
        api.timetable(date)
    }

    suspend fun name() = safeApiCall {
        api.name()
    }
}