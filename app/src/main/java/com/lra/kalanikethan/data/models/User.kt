package com.lra.kalanikethan.data.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val first_name: String,
    val last_name: String,
    val manager: Boolean,
    val uid: String
)


