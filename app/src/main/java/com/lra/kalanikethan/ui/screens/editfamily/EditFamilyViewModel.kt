package com.lra.kalanikethan.ui.screens.editfamily

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.FamilyWithID
import com.lra.kalanikethan.data.models.Parent
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.FamilyRepository
import com.lra.kalanikethan.data.repository.StudentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditFamilyViewModel(
    private val studentRepository: StudentRepository,
    private val familyRepository: FamilyRepository
) : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _parents = MutableStateFlow<List<Parent>>(emptyList())
    val parents: StateFlow<List<Parent>> = _parents

    private val _family = MutableStateFlow<FamilyWithID?>(null)
    val family: StateFlow<FamilyWithID?> = _family

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var nextLocalId = -1

    private val pendingNewStudents = mutableListOf<Student>()
    private val pendingNewParents = mutableListOf<Parent>()
    private val pendingUpdatedStudents = mutableMapOf<Int, Student>()
    private val pendingUpdatedParents = mutableMapOf<Int, Parent>()

    fun loadFamily(familyId: String) {
        _isLoading.value = true
        pendingNewStudents.clear()
        pendingNewParents.clear()
        pendingUpdatedStudents.clear()
        pendingUpdatedParents.clear()
        nextLocalId = -1
        viewModelScope.launch {
            try {
                _family.value = familyRepository.getFamilyFromID(familyId)
                _students.value = studentRepository.getStudentsByFamilyId(familyId)
                _parents.value = familyRepository.getParentsByFamilyId(familyId)
            } catch (e: Exception) {
                Log.e("EditFamily", "Error loading family: $e")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addStudentLocally() {
        val familyId = _family.value?.familyID ?: return
        val localId = nextLocalId--
        val student = Student(
            studentId = localId,
            familyId = familyId,
            firstName = "",
            lastName = "",
            birthdate = kotlinx.datetime.LocalDate(2000, 1, 1),
            canWalkAlone = false,
            dance = false,
            singing = false,
            music = false,
            signedIn = false
        )
        pendingNewStudents.add(student)
        _students.value = _students.value + student
    }

    fun addParentLocally() {
        val familyId = _family.value?.familyID ?: return
        val localId = nextLocalId--
        val parent = Parent(
            parentId = localId,
            familyId = familyId,
            firstName = "",
            lastName = "",
            phoneNumber = ""
        )
        pendingNewParents.add(parent)
        _parents.value = _parents.value + parent
    }

    fun confirmStudent(student: Student) {
        if (student.studentId != null && student.studentId < 0) {
            pendingNewStudents.replaceAll { if (it.studentId == student.studentId) student else it }
        } else {
            student.studentId?.let { pendingUpdatedStudents[it] = student }
        }
        _students.value = _students.value.map {
            if (it.studentId == student.studentId) student else it
        }
    }

    fun confirmParent(parent: Parent) {
        if (parent.parentId != null && parent.parentId < 0) {
            pendingNewParents.replaceAll { if (it.parentId == parent.parentId) parent else it }
        } else {
            parent.parentId?.let { pendingUpdatedParents[it] = parent }
        }
        _parents.value = _parents.value.map {
            if (it.parentId == parent.parentId) parent else it
        }
    }

    fun deleteStudent(student: Student) {
        if (student.studentId != null && student.studentId < 0) {
            pendingNewStudents.removeAll { it.studentId == student.studentId }
        } else {
            student.studentId?.let { id ->
                pendingUpdatedStudents.remove(id)
                viewModelScope.launch {
                    try {
                        studentRepository.deleteStudent(id)
                    } catch (e: Exception) {
                        Log.e("EditFamily", "Error deleting student: $e")
                    }
                }
            }
        }
        _students.value = _students.value.filter { it.studentId != student.studentId }
    }

    fun deleteParent(parent: Parent) {
        if (parent.parentId != null && parent.parentId < 0) {
            pendingNewParents.removeAll { it.parentId == parent.parentId }
        } else {
            parent.parentId?.let { id ->
                pendingUpdatedParents.remove(id)
                viewModelScope.launch {
                    try {
                        familyRepository.deleteParent(id)
                    } catch (e: Exception) {
                        Log.e("EditFamily", "Error deleting parent: $e")
                    }
                }
            }
        }
        _parents.value = _parents.value.filter { it.parentId != parent.parentId }
    }

    fun saveAll(familyName: String, email: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val currentFamily = _family.value ?: return@launch
                val updated = currentFamily.copy(familyName = familyName, email = email)
                familyRepository.updateFamily(updated)
                _family.value = updated

                for (student in pendingNewStudents) {
                    if (student.firstName.isNotBlank()) {
                        studentRepository.addStudent(student.copy(studentId = null))
                    }
                }

                for ((_, student) in pendingUpdatedStudents) {
                    studentRepository.updateStudent(student)
                }

                for (parent in pendingNewParents) {
                    if (parent.firstName.isNotBlank()) {
                        familyRepository.addParent(parent.copy(parentId = null))
                    }
                }

                for ((_, parent) in pendingUpdatedParents) {
                    familyRepository.updateParent(parent)
                }

                pendingNewStudents.clear()
                pendingNewParents.clear()
                pendingUpdatedStudents.clear()
                pendingUpdatedParents.clear()

                onComplete()
            } catch (e: Exception) {
                Log.e("EditFamily", "Error saving changes: $e")
            }
        }
    }
}
