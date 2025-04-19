package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Parent(
    val parentId: Int,
    val familyId: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)