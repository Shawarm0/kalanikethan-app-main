package com.lra.kalanikethan.ui.screens.Add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.Repository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

class AddViewModel(
    private val repository: Repository
): ViewModel() {
    fun createFamily(paymentData: PaymentData, parents: List<Parent>, students: List<Student>) {
        var id : String? = null

        val formatter = LocalDate.Format { day(); char('/'); monthNumber(); char('/') ; year() }

        val family = Family(
            familyName = paymentData.familyName,
            email = paymentData.email
        )
        viewModelScope.launch {
            try{
                id = repository.addFamily(family)
                val plan = PaymentPlan(id as String, paymentData.amount.toFloat(), LocalDate.parse(paymentData.paymentDate, formatter).day, paymentData.paymentId)
                repository.addPaymentData(plan)
                for (parent in parents) {
                    parent.familyId = id as String
                    repository.addParent(parent)
                }
                for (student in students) {
                    student.familyId = id as String
                    repository.addStudent(student)
                }
            } catch (e: Exception) {
                Log.e("Database", "Error while attempting to add family : $e")
            }
        }
    }
}