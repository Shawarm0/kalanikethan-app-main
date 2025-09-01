package com.lra.kalanikethan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Employee (
    @SerialName("uid") val uid: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("manager") val manager: Boolean,
)