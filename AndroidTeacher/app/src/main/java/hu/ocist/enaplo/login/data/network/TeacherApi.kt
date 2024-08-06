package hu.ocist.enaplo.login.data.network

import hu.ocist.enaplo.login.data.requests.*
import hu.ocist.enaplo.login.data.responses.*
import retrofit2.http.*


interface TeacherApi {
    @POST("teacher/grade")
    suspend fun sendGrade(
        @Body addGradeRequest: AddGradeRequest
    ): StringResponse

    @POST("teacher/grades")
    suspend fun sendGrades(
        @Body addGradesRequest: List<AddGradeRequest>
    ): StringResponse

    @POST("teacher/admonitory")
    suspend fun admonitoryToStudent(
        @Body newJudgementRequest: NewJudgementRequest
    ): StringResponse

    @GET("teacher/admonitory/types")
    suspend fun admonitoryTypes(): List<IntStringResponse>

    @GET("teacher/messages/{messageId}")
    suspend fun message(
        @Path("messageId") messageid: Int
    ): MessageTeacherResponse

    @GET("teacher/outmessages/group/{messageId}")
    suspend fun groupMessageMembers(
        @Path("messageId") messageId: Int
    ): List<String>

    @POST("teacher/message/group")
    suspend fun sendMessageToGroup(
        @Body messageToTeacherDto: MessageToGroupRequest
    ): StringResponse

    @POST("teacher/message/teacher")
    suspend fun sendMessageToTeacher(
        @Body postMessageTeacherRequest: PostMessageTeacherRequest
    ): StringResponse

    @GET("teacher/messages")
    suspend fun messages(): List<MessageResponse>

    @GET("teacher/outmessages/teacher")
    suspend fun outboxMessages(): List<MessageResponse>

    @GET("teacher/outmessages/group")
    suspend fun messagesSentToGroup(): List<MessageGroupResponse>

    @POST("teacher/propitious")
    suspend fun propitiousToStudent(
        @Body newJudgementRequest: NewJudgementRequest
    ): StringResponse

    @GET("teacher/propitious/types")
    suspend fun propitiousTypes(): List<IntStringResponse>

    @GET("teacher/registerlesson")
    suspend fun getRegisterLesson(
        @Query("date") date: String,
        @Query("numberoflesson") numberOfLesson: Int,
        @Query("dividendid") dividendId: Int
    ): RegisterLessonResponse

    @POST("teacher/registerlesson")
    suspend fun postRegisterLesson(
        @Body registerLessonRequest: RegisterLessonRequest
    ): StringResponse

    @GET("teacher/groups")
    suspend fun taughtGroups(): List<IntStringResponse>

    @GET("teacher/groupmembers/{groupId}")
    suspend fun groupMembers(
        @Path("groupId") groupId: Int
    ): List<IntStringResponse>

    @GET("teacher/teachers")
    suspend fun teachers(): List<IntStringResponse>

    @GET("teacher/timetable/{date}")
    suspend fun timetable(
        @Path("date") date: String
    ): List<TimetableResponse>

    @GET("teacher/absence")
    suspend fun getMissingStudents(
        @Query("lessonid") lessonId: Int,
        @Query("dividendid") dividendId: Int
    ): List<MissingResponse>

    @POST("teacher/absence")
    suspend fun postMissingStudents(
        @Body missingRequest: MissingRequest
    ): StringResponse

    @GET("teacher/late")
    suspend fun getLate(
        @Query("lessonid") lessonId: Int,
        @Query("dividendid") dividendId: Int
    ): List<LateResponse>

    @POST("teacher/late")
    suspend fun postLate(
        @Body lateRequest: LateRequest
    ): StringResponse

    @GET("teacher/name")
    suspend fun name(): StringResponse

    // it's actually not a teacher api, I just didn't wanted to do an own api for canteen
    @GET("canteen/{date}")
    suspend fun canteen(
        @Path("date") date: String? = null
    ): List<CanteenResponse>

    @POST("canteen")
    suspend fun postCanteen(
        @Body postCanteenResponse: CanteenRequest
    ): CanteenResponse

    @DELETE("canteen/{id}")
    suspend fun deleteCanteen(
        @Path("id") id: Int
    ): StringResponse

    @GET("teacher/grade/types")
    suspend fun gradeTypes(): List<GradeTypeResponse>

    @GET("teacher/class")
    suspend fun getMyClass(): MyClassResponse

    @POST("teacher/class")
    suspend fun postMyClass(
        @Body myClassResponse: MyClassResponse
    ): StringResponse

    @GET("teacher/propitious")
    suspend fun propitiouses(): List<JudgementResponse>

    @GET("teacher/admonitory")
    suspend fun admonitories(): List<JudgementResponse>

    @GET("teacher/subjects/{groupId}")
    suspend fun subjects(
        @Path("groupId") groupId: Int
    ): List<IntStringResponse>

    @GET("teacher/sumgrades/{dividendid}")
    suspend fun sumGrades(
        @Path("dividendid") dividendId: Int
    ): List<SumGradesResponse>



    @GET("teacher/grades")
    suspend fun grades(
        @Query("semester") semester: String,
        @Query("subject") subject: String,
        @Query("studentid") studentId: Int,
    ): List<GradesResponse>
}
