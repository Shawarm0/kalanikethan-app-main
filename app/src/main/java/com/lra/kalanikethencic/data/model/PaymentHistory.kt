package com.lra.kalanikethencic.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PaymentHistory(
    val paymentId: Int,
    val familyId: Int,
    val date: String,
    val amount: Float,
    val paid: Boolean
)
