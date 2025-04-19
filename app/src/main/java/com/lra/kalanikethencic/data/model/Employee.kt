package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable


@Serializable
data class Employee(
    val employeeId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val manager: Boolean
)
