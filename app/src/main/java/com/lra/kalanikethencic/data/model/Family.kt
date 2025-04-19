package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Family(
    val familyId: Int,
    val familyName: String,
    val email: String,
    val familyPaymentId: String
)