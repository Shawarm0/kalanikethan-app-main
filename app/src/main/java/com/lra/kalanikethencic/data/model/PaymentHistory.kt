package com.lra.kalanikethencic.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentHistory(
    @SerialName("payment_id")
    val paymentId: Int,
    @SerialName("family_id")
    val familyId: Int,
    val date: String,
    val amount: Float,
    val paid: Boolean
)
