package com.lra.kalanikethan.data.repository

import android.util.Log
import com.lra.kalanikethan.data.models.FamilyWithID
import com.lra.kalanikethan.data.models.PaymentHistory
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.util.Tables
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

class PaymentRepository {
    private val client = SupabaseClientProvider.client

    suspend fun getPlanFromID(id: String): PaymentPlan {
        return client.from(Tables.PAYMENT_PLAN).select {
            filter {
                PaymentPlan::family_payment_id eq id
            }
        }.decodeSingle<PaymentPlan>()
    }

    suspend fun addFirstFamilyPayment(paymentID: String, date: LocalDate, amount: Float) {
        client.from(Tables.PAYMENT_HISTORY).insert(
            PaymentHistory(family_payment_id = paymentID, paid = false, due_date = date, amount = amount)
        )
    }

    suspend fun getUnpaidPayments(): List<PaymentHistory> {
        return client.from(Tables.PAYMENT_HISTORY).select {
            order(column = "due_date", order = Order.ASCENDING)
            filter {
                PaymentHistory::paid eq false
            }
        }.decodeList<PaymentHistory>()
    }

    suspend fun confirmPayment(id: Int, amount: Float) {
        client.from(Tables.PAYMENT_HISTORY).update(
            {
                PaymentHistory::paid setTo true
                PaymentHistory::amount setTo amount
            }
        ) {
            filter {
                PaymentHistory::payment_id eq id
            }
        }
    }

    suspend fun getLatestPaymentFromFamilyID(id: String): PaymentHistory {
        Log.i("Database", "Filtering history with ID : $id")
        return client.from(Tables.PAYMENT_HISTORY).select {
            filter {
                PaymentHistory::family_payment_id eq id
            }
            order(column = "due_date", order = Order.DESCENDING)
        }.decodeSingle<PaymentHistory>()
    }

    suspend fun addPaymentToFamily(payment: PaymentHistory, familyID: String) {
        val plan = getPlanFromID(familyID)
        val adjustedDate = LocalDate(payment.due_date.year, payment.due_date.month, plan.payment_date)
        val newPayment = payment.copy(
            due_date = adjustedDate.plus(1, DateTimeUnit.MONTH),
            amount = plan.amount,
            paid = false,
            payment_id = null
        )
        client.from(Tables.PAYMENT_HISTORY).insert(newPayment)
    }

    suspend fun getFamilyPaymentHistory(familyID: String): List<PaymentHistory> {
        return client.from(Tables.PAYMENT_HISTORY).select {
            filter {
                PaymentHistory::family_payment_id eq familyID
            }
            order(column = "due_date", order = Order.DESCENDING)
        }.decodeList<PaymentHistory>()
    }

    suspend fun addPaymentData(paymentPlan: PaymentPlan) {
        client.from(Tables.PAYMENT_PLAN).insert(paymentPlan)
    }
}
