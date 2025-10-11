package com.lra.kalanikethan.ui.screens.Add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Family
import com.lra.kalanikethan.data.models.FamilyPayments
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.PaymentHistory
import com.lra.kalanikethan.data.models.PaymentPlan
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import kotlinx.datetime.minus

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

    fun createParent(){
        _parents.update {
            parents -> newParent(parents)
        }
    }

    fun deleteParent(parent : Parent){
        _parents.update {
                parents -> removeParentFromList(parents, parent)
        }
    }

    fun updateParent(index : Int, newParent: Parent){
        _parents.update {
                parents -> updateParentInList(parents, index = index, newParent)
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
                val date = LocalDate.parse(paymentData.paymentDate, formatter)
                val plan = PaymentPlan(id as String, paymentData.amount.toFloat(), date.day, paymentData.paymentId)
                repository.addPaymentData(plan)
                for (parent in parents) {
                    parent.familyId = id as String
                    repository.addParent(parent)
                }
                for (student in students) {
                    student.familyId = id as String
                    repository.addStudent(student)
                }
                repository.addFirstFamilyPayment(paymentData.paymentId, date, paymentData.amount.toFloat())
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

    private fun newParent(list : List<Parent>) : List<Parent>{
        val newList = list.toMutableList()
        newList.add(
            Parent(
                familyId = "",
                firstName = "",
                lastName = "",
                phoneNumber = "",
                internalID = nextID
            )
        )
        nextID += 1
        return newList.toList()
    }

    private fun removeStudentFromList(list : List<Student>, student : Student) : List<Student>{
        val newList = list.toMutableList()
        newList.remove(student)
        return newList.toList()
    }

    private fun removeParentFromList(list : List<Parent>, parent : Parent) : List<Parent>{
        val newList = list.toMutableList()
        newList.remove(parent)
        return newList.toList()
    }

    private fun updateStudentInList(list : List<Student>, index: Int, student: Student) : List<Student>{
        val newList = list.toMutableList()
        newList.set(index, student)
        return newList.toList()
    }

    private fun updateParentInList(list : List<Parent>, index: Int, Parent: Parent) : List<Parent>{
        val newList = list.toMutableList()
        newList.set(index, Parent)
        return newList.toList()
    }
}