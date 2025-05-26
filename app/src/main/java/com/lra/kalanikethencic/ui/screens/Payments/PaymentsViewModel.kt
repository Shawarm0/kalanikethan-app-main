package com.lra.kalanikethencic.ui.screens.Payments

import android.icu.util.Calendar
import androidx.annotation.OptIn
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewModelScope
import androidx.media3.common.util.UnstableApi
import com.lra.kalanikethencic.data.model.PaymentHistory
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.model.StudentClassComposable
import com.lra.kalanikethencic.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import android.util.Log
import androidx.compose.ui.input.pointer.PointerId
import com.lra.kalanikethencic.data.model.Family

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _payments = MutableStateFlow<List<PaymentHistory>>(emptyList())
    val payments: StateFlow<List<PaymentHistory>> = _payments

    private val _familyNames = MutableStateFlow<List<Family>>(emptyList())
    val familyNames: StateFlow<List<Family>> = _familyNames


    private var debounceJob: Job? = null
    private val pendingUpdates = mutableMapOf<Int, Student>()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading


    fun preloadPayments() {
        if (_payments.value.isEmpty()) {
            loadPayments()
        }
    }

    fun preLoadFamilies() {
        viewModelScope.launch {
            repository.getFamilyNames()
                .catch{ e -> Log.e("PaymentsViewModel", "Error loading families", e)}
                .collect{ families ->
                    _familyNames.value = families
                }
        }
    }

    @OptIn(UnstableApi::class)
    private fun loadPayments() { //load only overdue and unpaid payments
        viewModelScope.launch {
            repository.getPaymentHistory()
                .map { paymentsList ->
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val calendar = Calendar.getInstance() //current date and time
                    calendar.add(Calendar.DAY_OF_YEAR ,-30) //subtract 30 days from current date
                    val thirtyDaysAgo = calendar.time

                    paymentsList.filter { payment ->
                        try{
                            val paymentDate = dateFormat.parse(payment.date)
                            !payment.paid && paymentDate != null && paymentDate.before(thirtyDaysAgo)
                        }
                        catch (e:Exception) {
                            false
                        }
                    }  //filter for unpaid payments and overdue payments (i.e. been standing for > 30  days)
                }
                .catch { e ->
                    // handle errors here if needed
                    println("Error loading payments: ${e.message}")
                }
                .collect { filteredList ->
                    _payments.value = filteredList  //update with filtered list
                }
        }
    }

    fun idToName(familyId: Int): Family? {
        return familyNames.value.find { it.familyId == familyId }
    }
}