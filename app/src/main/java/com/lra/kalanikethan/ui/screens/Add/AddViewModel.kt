package com.lra.kalanikethan.ui.screens.Add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.FamilyPayments
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char

class AddViewModel(
    private val repository: Repository
): ViewModel() {
    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students : StateFlow<List<Student>> = _students

    var nextID : Int = 0

    private val _parents = MutableStateFlow<List<Parent>>(emptyList())
    val parents : StateFlow<List<Parent>> = _parents

    fun createStudent(){
        _students.update {
            students -> newStudent(students)
        }
    }

    fun deleteStudent(student: Student){
        _students.update {
            students -> removeStudentFromList(students, student)
        }
    }

    fun updateStudent(index : Int, newStudent: Student){
        _students.update {
            students -> updateStudentInList(students, index, newStudent)
        }
    }

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

    private fun newStudent(list : List<Student>) : List<Student>{
        val newList = list.toMutableList()
        newList.add(
            Student(
                familyId = "",
                firstName = "",
                lastName = "",
                birthdate = LocalDate(2000, 1, 1),
                canWalkAlone = false,
                dance = false,
                singing = false,
                music = false,
                signedIn = false,
                internalID = nextID
            )
        )
        nextID += 1
        return newList.toList()
    }

    private fun removeStudentFromList(list : List<Student>, student : Student) : List<Student>{
        val newList = list.toMutableList()
        println("Deleting from $list")
        println("Deleting student $student")
        newList.remove(student)
        return newList.toList()
    }

    private fun updateStudentInList(list : List<Student>, index: Int, student: Student) : List<Student>{
        val newList = list.toMutableList()
        newList.set(index, student)
        return newList.toList()
    }
}