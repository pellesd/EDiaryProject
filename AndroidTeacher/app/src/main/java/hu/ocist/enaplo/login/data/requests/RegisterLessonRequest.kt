@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegisterLessonRequest (
    val lessonId: kotlin.Int,
    val day: kotlin.String,
    val numberOfLesson: kotlin.Int,
    val date: kotlin.String,
    val teacherId: kotlin.Int,
    val groupId: kotlin.Int,
    val dividendId: kotlin.Int,
    val subjectId: kotlin.Int,
    val lessonDescriptionId: kotlin.Int? = null,
    val lessonDescription: kotlin.String,
    val deleted: kotlin.Boolean,
    val dated: kotlin.String,
    val shouldGrade: kotlin.Boolean
)
