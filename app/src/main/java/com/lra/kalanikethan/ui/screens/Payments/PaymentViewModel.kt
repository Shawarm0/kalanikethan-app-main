package com.lra.kalanikethan.ui.screens.Payments

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.Screen
import com.lra.kalanikethan.data.models.FamilyPayments
import com.lra.kalanikethan.data.repository.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import kotlinx.datetime.plus

class PaymentViewModel(
    val repository: Repository
) : ViewModel() {
    private val _unpaidFamilies = MutableStateFlow<List<FamilyPayments>>(emptyList())
    val unpaidFamilies : StateFlow<List<FamilyPayments>> = _unpaidFamilies
    private var unpaidDebounceJob: Job? = null

    private val _currentFamily = mutableStateOf(PaymentHistoryData())
    val currentFamily = _currentFamily

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
                    payments -> filterPayments(payments, finalList)
            }
        }
    }

    fun confirmPayment(id : Int, familyId : String){
        viewModelScope.launch {
            try{
                repository.confirmPayment(id)
                addNewPayment(familyId)
                _unpaidFamilies.update { list -> removePaymentFromList(id, list) }
            } catch (e : Exception){
                Log.e("Database", "Failed to confirm payment with id $id : $e")
            }
        }
    }

    fun addNewPayment(familyId: String){
        viewModelScope.launch {
            val payment = repository.getLatestPaymentFromFamilyID(familyId)
            if(payment.paid){
                repository.addPaymentToFamily(payment)
            }
        }
    }

    fun updateCurrentFamily(familyId: String, familyName : String, amount : String){
        viewModelScope.launch {
            val list = repository.getFamilyPaymentHistory(familyId)
            _currentFamily.value = PaymentHistoryData(history = list, familyName = familyName, familyID = familyId, amount = amount)
        }
    }

    private fun removePaymentFromList(id: Int, list: List<FamilyPayments>) : List<FamilyPayments>{
        val mutable = list.toMutableList()
        val toRemove : MutableList<FamilyPayments> = mutableListOf()
        for (payment in mutable){
            if(payment.currentPayment.payment_id == id){
                toRemove.add(payment)
            }
        }
        mutable.removeAll(toRemove)
        return mutable.toList()
    }

    private fun filterPayments(original : List<FamilyPayments>, new : MutableList<FamilyPayments>) : List<FamilyPayments>{
        val mutable = original.toMutableList()

        mutable.clear()
        mutable.addAll(new)

        return mutable.toList()
    }
}