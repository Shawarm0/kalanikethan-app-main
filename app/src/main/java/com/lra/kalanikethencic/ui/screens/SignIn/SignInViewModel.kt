package com.lra.kalanikethencic.ui.screens.SignIn

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.lra.kalanikethencic.database.models.Student
import com.lra.kalanikethencic.database.respository.DummyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: DummyRepository
) : ViewModel() {

    private val _students = mutableStateOf(repository.getStudents())
    val students: State<List<Student>> = _students

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery


    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _students.value = repository.getStudents().filter {
            it.firstName.contains(query, ignoreCase = true) ||
                    it.lastName.contains(query, ignoreCase = true)
        }
    }

    fun toggleSignedIn(studentId: Int) {
        _students.value = _students.value.map { student ->
            if (student.studentId == studentId) {
                println("Changed sign-in status for student: ${student.firstName} ${student.lastName}")
                student.copy(signedIn = !student.signedIn)
            } else {
                student
            }
        }

    }
}