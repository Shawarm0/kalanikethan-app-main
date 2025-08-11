package com.lra.kalanikethan.data.models

import com.lra.kalanikethan.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PaymentPlan(
    val family_id : String,
    val amount : Float,
    @Serializable(with = LocalDateSerializer::class)
    val payment_date : LocalDate,
    val family_payment_id : String
)