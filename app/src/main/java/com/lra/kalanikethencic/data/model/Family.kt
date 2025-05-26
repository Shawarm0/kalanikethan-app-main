package com.lra.kalanikethencic.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Family(
    @SerialName("family_id")
    val familyId: Int,
    @SerialName("family_name")
    val familyName: String,
    val email: String,
    @SerialName("family_payment_id")
    val familyPaymentId: String
)