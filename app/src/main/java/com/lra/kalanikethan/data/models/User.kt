package com.lra.kalanikethan.data.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val first_name: String,
    val last_name: String,
    val manager: Boolean,
    val uid: String
)

var sessionPermissions: MutableState<User> = mutableStateOf(User("None", "", false, ""))

var authCompleted = false