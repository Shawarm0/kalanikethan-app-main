package com.lra.kalanikethan.data.models

import com.lra.kalanikethan.util.LocalDateSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class PaymentPlan(
    val family_id : String,
    val amount : Float,
    val payment_date : Int,
    val family_payment_id : String
)

data class FamilyPayments(
    val family : FamilyWithID,
    val currentPayment : PaymentHistory,
    val pastPayments : List<PaymentHistory>? = null,
    val paymentPlan : PaymentPlan
)