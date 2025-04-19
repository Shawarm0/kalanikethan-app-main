package com.lra.kalanikethencic.ui.screens.SignIn

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _students = mutableStateOf<List<Student>>(emptyList())
    val students: State<List<Student>> = _students

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    init {
        loadStudents()
    }

    fun preloadStudents() {
        if (_students.value.isEmpty()) {
            loadStudents()
        }
    }


    private fun loadStudents() {
        viewModelScope.launch {
            try {
                val allStudents = repository.getAllStudents()
                _students.value = allStudents
            } catch (e: Exception) {
                // handle error (e.g., show snackbar, log, etc.)
                println("Error fetching students: ${e.message}")
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        val originalList = _students.value
        _students.value = originalList.filter {
            it.firstName.contains(query, ignoreCase = true) ||
                    it.lastName.contains(query, ignoreCase = true)
        }
    }

    fun toggleSignedIn(studentId: Int) {
        val updatedList = _students.value.map { student ->
            if (student.studentId == studentId) {
                println("Changed sign-in status for student: ${student.firstName} ${student.lastName}")
                student.copy(signedIn = !student.signedIn)
            } else student
        }

        _students.value = updatedList

        viewModelScope.launch {
            val updatedStudent = updatedList.find { it.studentId == studentId }
            if (updatedStudent != null) {
                repository.updateStudent(updatedStudent)
            }
        }
    }
}
