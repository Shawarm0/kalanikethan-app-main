package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class StudentClass(
    val studentId: Int,
    val classId: Int
)