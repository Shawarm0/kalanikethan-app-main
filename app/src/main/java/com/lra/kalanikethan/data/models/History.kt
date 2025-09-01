package com.lra.kalanikethan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class History (
    @SerialName("history_id") val historyID: Int? = null,
    @SerialName("student_id") var studentID: Int?,
    @SerialName("class_id") val classID: Int? = null,
    @SerialName("day") val date: String,
    @SerialName("sign_in_time") val signInTime: Long,
    @SerialName("sign_out_time") val signOutTime: Long? = null,
    @SerialName("uid") val uid: String? = null,
)