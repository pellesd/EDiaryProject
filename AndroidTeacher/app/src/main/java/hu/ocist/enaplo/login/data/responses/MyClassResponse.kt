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
    val groupId: Int,
    val headTeacher: String,
    val subHeadTeacher: String,
    var sevenId1: Int?,
    val seven1: String?,
    var sevenId2: Int?,
    val seven2: String?,
)