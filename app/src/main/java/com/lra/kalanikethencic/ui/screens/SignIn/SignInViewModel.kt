package com.lra.kalanikethencic.ui.screens.SignIn

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _students = MutableStateFlow<List<Student>>(emptyList())
    val students: StateFlow<List<Student>> = _students

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private var studentsJob: Job? = null

    init {
        loadStudents()
    }

    fun preloadStudents() {
        if (_students.value.isEmpty()) {
            loadStudents()
        }
    }


    private fun loadStudents() {
        // Cancel any existing collection
        studentsJob?.cancel()

        studentsJob = viewModelScope.launch {
            repository.getAllStudents()
                .catch { e ->
                    // Handle errors
                    println("Error in student flow: ${e.message}")
                }
                .collect { studentList ->
                    _students.value = studentList
                }
        }
    }
//
//    fun onSearchQueryChanged(query: String) {
//        _searchQuery.value = query
//        val originalList = _students.value
//        _students.value = originalList.filter {
//            it.firstName.contains(query, ignoreCase = true) ||
//                    it.lastName.contains(query, ignoreCase = true)
//        }
//    }
//
//    fun toggleSignedIn(studentId: Int) {
//        val updatedList = _students.value.map { student ->
//            if (student.studentId == studentId) {
//                println("Changed sign-in status for student: ${student.firstName} ${student.lastName}")
//                student.copy(signedIn = !student.signedIn)
//            } else student
//        }
//
//        _students.value = updatedList
//
//        viewModelScope.launch {
//            val updatedStudent = updatedList.find { it.studentId == studentId }
//            if (updatedStudent != null) {
//                repository.updateStudent(updatedStudent)
//            }
//        }
//    }
}
