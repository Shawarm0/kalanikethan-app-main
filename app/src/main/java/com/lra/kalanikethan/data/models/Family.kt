package com.lra.kalanikethan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Family(
    @SerialName("family_name")
    val familyName: String,
    val email: String,
)

@Serializable
data class FamilyWithID(
    @SerialName("family_id")
    val familyID : String,
    @SerialName("family_name")
    val familyName: String,
    val email: String,
)