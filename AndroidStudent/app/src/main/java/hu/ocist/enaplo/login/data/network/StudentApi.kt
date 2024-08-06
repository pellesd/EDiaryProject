package hu.ocist.enaplo.login.data.network

import hu.ocist.enaplo.login.data.responses.*
import retrofit2.http.*


interface StudentApi {

    @GET("student/absence")
    suspend fun absences(): List<AbsenceResponse>

    @GET("student/admonitory")
    suspend fun admonitory(): List<JudgementResponse>

    @GET("student/class")
    suspend fun myClass(): MyClassResponse

    @GET("student/exams")
    suspend fun exams(): List<ExamResponse>

    @GET("student/grades")
    suspend fun grades(@Query("semester") semester: String, @Query("subject") subject: String): List<GradesResponse>

    @GET("student/groupmembers/{group}")
    suspend fun groupMembers(
        @Path("group") group: String
    ): List<String>

    @GET("student/late")
    suspend fun late(): List<LateResponse>

    @GET("student/messages")
    suspend fun messages(): List<MessageResponse>

    @GET("student/propitious")
    suspend fun propitious(): List<JudgementResponse>

    @GET("student/sumgrades")
    suspend fun sumGrades(): List<SumGradesResponse>

    @GET("student/timetable/{date}")
    suspend fun timetable(@Path("date") date: String? = null): List<TimetableResponse>

    @GET("student/name")
    suspend fun name(): StringResponse

    // it's actually not a student api, I just didn't wanted to do an own api for this single endpoint
    @GET("canteen/{date}")
    suspend fun canteen(@Path("date") date: String? = null): List<CanteenResponse>
}
