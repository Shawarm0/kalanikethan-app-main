package com.lra.kalanikethan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentClass(
    @SerialName("student_id") val studentId: Int,
    @SerialName("class_id") val classId: Int
)