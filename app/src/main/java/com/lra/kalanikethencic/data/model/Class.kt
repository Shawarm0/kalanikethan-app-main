package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Class(
    val classId: Int,
    val teacherName: String,
    val type: String,
    val startTime: Long,
    val endTime: Long
)