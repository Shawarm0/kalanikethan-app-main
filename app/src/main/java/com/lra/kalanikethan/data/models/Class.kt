package com.lra.kalanikethan.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Class(
    @SerialName("class_id") val classId: Int,
    @SerialName("teacher_name") var teacherName: String,
    @SerialName("type") var type: String,
    @SerialName("start_time") var startTime: String,
    @SerialName("end_time") var endTime: String
)