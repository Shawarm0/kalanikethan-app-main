package com.lra.kalanikethan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lra.kalanikethan.data.repository.AttendanceRepository
import com.lra.kalanikethan.data.repository.ClassRepository
import com.lra.kalanikethan.data.repository.EmployeeRepository
import com.lra.kalanikethan.data.repository.FamilyRepository
import com.lra.kalanikethan.data.repository.HistoryRepository
import com.lra.kalanikethan.data.repository.PaymentRepository
import com.lra.kalanikethan.data.repository.StudentRepository
import com.lra.kalanikethan.ui.screens.Add.AddViewModel
import com.lra.kalanikethan.ui.screens.Payments.PaymentViewModel
import com.lra.kalanikethan.ui.screens.editfamily.EditFamilyViewModel

class AppViewModelFactory(
    private val studentRepository: StudentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val classRepository: ClassRepository,
    private val historyRepository: HistoryRepository,
    private val employeeRepository: EmployeeRepository,
    private val familyRepository: FamilyRepository,
    private val paymentRepository: PaymentRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AttendanceViewModel::class.java) ->
            AttendanceViewModel(studentRepository, attendanceRepository, historyRepository) as T
        modelClass.isAssignableFrom(ClassManagementViewModel::class.java) ->
            ClassManagementViewModel(classRepository) as T
        modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
            HistoryViewModel(historyRepository, employeeRepository) as T
        modelClass.isAssignableFrom(AddViewModel::class.java) ->
            AddViewModel(studentRepository, familyRepository, paymentRepository) as T
        modelClass.isAssignableFrom(PaymentViewModel::class.java) ->
            PaymentViewModel(paymentRepository, familyRepository) as T
        modelClass.isAssignableFrom(EditFamilyViewModel::class.java) ->
            EditFamilyViewModel(studentRepository, familyRepository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
