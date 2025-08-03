package com.lra.kalanikethan.ui.screens.Add

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

    val familyID = mutableIntStateOf(1)

    fun getFamilyID(): Int {
        viewModelScope.launch {
            familyID.intValue = repository.getLastFamilyID()
        }
        return familyID.intValue
    }


    fun createFamily(paymentData: PaymentData, parents: List<Parent>, students: List<Student>) {

        val family = Family(
            familyId = 1, // hopefully this overrights
            familyName = paymentData.familyName,
            email = paymentData.email,
        )
        viewModelScope.launch {
            repository.addFamily(family)
        }

        viewModelScope.launch {
            for (parent in parents) {
                repository.addParent(parent)
            }
        }

        viewModelScope.launch {
            for (student in students) {
                repository.addStudent(student)
            }
        }



    }





}