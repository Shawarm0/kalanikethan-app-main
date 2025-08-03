package com.lra.kalanikethan.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parent(
    @SerialName("parent_id") val parentId: Int? = null,
    @SerialName("family_id") var familyId: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    @SerialName("phone_number") val phoneNumber: String? = null
)