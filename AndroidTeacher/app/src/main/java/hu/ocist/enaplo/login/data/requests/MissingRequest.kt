package hu.ocist.enaplo.login.data.requests

import hu.ocist.enaplo.login.data.responses.MissingResponse

data class MissingRequest(
    val lessonId: Int,
    val dividendId: Int,
    val date: String,
    val absences: List<MissingResponse>
)
