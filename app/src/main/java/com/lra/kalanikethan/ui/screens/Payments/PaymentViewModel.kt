package com.lra.kalanikethan.ui.screens.Payments

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.FamilyPayments
import com.lra.kalanikethan.data.models.FamilyWithID
import com.lra.kalanikethan.data.models.PaymentHistory
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.remote.SupabaseClientProvider
import com.lra.kalanikethan.data.repository.Repository
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentViewModel(
    val repository: Repository
) : ViewModel() {
    private val _unpaidFamilies = MutableStateFlow<List<FamilyPayments>>(emptyList())
    val unpaidFamilies : StateFlow<List<FamilyPayments>> = _unpaidFamilies
    private var unpaidDebounceJob: Job? = null

    fun getUnpaidPayments(){
        unpaidDebounceJob?.cancel()
        unpaidDebounceJob = viewModelScope.launch {
            val list = repository.getUnpaidPayments()
            Log.i("Database", "Got due payments : $list")
            val finalList = mutableListOf<FamilyPayments>()
            for (payment in list){
                val paymentPlan = repository.getPlanFromID(payment.family_payment_id)
                val family = repository.getFamilyFromID(paymentPlan.family_id)
                finalList.add(FamilyPayments(family = family, currentPayment = payment, paymentPlan = paymentPlan))
                Log.i("Database", "Added due payment of ${payment.family_payment_id}")
            }
            _unpaidFamilies.update {
                    list -> (list + finalList.toList()).distinctBy { it.currentPayment.payment_id }
            }
        }
    }
}