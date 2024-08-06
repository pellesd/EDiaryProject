@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package hu.ocist.enaplo.login.data.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MyClassResponse(
    val name: String,
    val headTeacher: String,
    val subHeadTeacher: String,
    val seven1: String?,
    val seven2: String?,
)