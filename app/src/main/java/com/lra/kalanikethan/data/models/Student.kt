package com.lra.kalanikethan.data.models

import androidx.compose.runtime.Immutable
import com.lra.kalanikethan.util.LocalDateSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Immutable
@Serializable
data class Student(
    @SerialName("student_id") val studentId: Int? = null,
    @SerialName("family_id") var familyId: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthdate: kotlinx.datetime.LocalDate,
    @SerialName("can_walk_alone") val canWalkAlone: Boolean,
    val dance: Boolean,
    val singing: Boolean,
    val music: Boolean,
    @SerialName("signed_in") val signedIn: Boolean
)