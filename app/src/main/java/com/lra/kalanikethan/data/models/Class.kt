package com.lra.kalanikethan.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Class(
    @SerialName("class_id") val classId: Int,
    @SerialName("teacher_name") val teacherName: String,
    @SerialName("type") val type: String,
    @SerialName("start_time") val startTime: Long,
    @SerialName("end_time") val endTime: Long
)