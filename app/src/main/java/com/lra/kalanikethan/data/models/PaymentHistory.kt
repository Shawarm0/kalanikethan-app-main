package com.lra.kalanikethan.data.models

import com.lra.kalanikethan.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PaymentHistory(
    var payment_id : Int? = null,
    val family_payment_id : String,
    var paid : Boolean,
    @Serializable(with = LocalDateSerializer::class)
    var due_date : LocalDate
)
