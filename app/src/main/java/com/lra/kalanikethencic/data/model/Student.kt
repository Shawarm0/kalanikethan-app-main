package com.lra.kalanikethencic.data.model

import com.lra.kalanikethencic.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Student(
    @SerialName("student_id") val studentId: Int,
    @SerialName("family_id") val familyId: Int,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthdate: LocalDate,
    @SerialName("can_walk_alone") val canWalkAlone: Boolean,
    val dance: Boolean,
    val singing: Boolean,
    val music: Boolean,
    @SerialName("signed_in") val signedIn: Boolean
)
