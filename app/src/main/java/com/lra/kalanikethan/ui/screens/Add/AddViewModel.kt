package com.lra.kalanikethan.ui.screens.Add

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.Repository
import kotlinx.coroutines.launch

class AddViewModel(
    private val repository: Repository
): ViewModel() {
    fun createFamily(paymentData: PaymentData, parents: List<Parent>, students: List<Student>) {
        var id : String? = null

        val family = Family(
            familyName = paymentData.familyName,
            email = paymentData.email
        )
        viewModelScope.launch {
            try{
                id = repository.addFamily(family)
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