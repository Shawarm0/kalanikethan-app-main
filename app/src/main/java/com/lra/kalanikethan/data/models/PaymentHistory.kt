package com.lra.kalanikethan.data.models

import com.lra.kalanikethan.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PaymentHistory(
    val payment_id : Int? = null,
    val family_payment_id : String,
    val paid : Boolean,
    @Serializable(with = LocalDateSerializer::class)
    val due_date : LocalDate
)
