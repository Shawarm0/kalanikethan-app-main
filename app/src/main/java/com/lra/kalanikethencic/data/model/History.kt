package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class History(
    val historyId: Int,
    val studentId: Int,
    val classId: Int,
    val day: String,
    val signInTime: Long,
    val signOutTime: Long,
    val employeeId: Int
)
