@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.requests

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LessonKeysRequest(
    var date: kotlin.String,
    val numberOfLesson: kotlin.Int,
    val dividendId: kotlin.Int,
    val groupId: kotlin.Int,
)