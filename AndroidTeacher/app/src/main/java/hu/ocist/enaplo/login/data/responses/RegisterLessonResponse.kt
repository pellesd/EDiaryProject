@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterLessonResponse (
    val registerLessonId: kotlin.Int,
    val lessonId: kotlin.Int,
    val day: kotlin.String,
    val numberOfLesson: kotlin.Int,
    val date: kotlin.String,
    var teacherId: kotlin.Int,
    val teacher: kotlin.String,
    var groupId: kotlin.Int,
    val group: kotlin.String,
    val dividendId: kotlin.Int,
    val subjectId: kotlin.Int,
    val subject: kotlin.String,
    val lessonDescriptionId: kotlin.Int? = null,
    var lessonDescription: kotlin.String? = null,
    val deleted: kotlin.Boolean,
    val dated: kotlin.String,
    val shouldGrade: kotlin.Boolean
)