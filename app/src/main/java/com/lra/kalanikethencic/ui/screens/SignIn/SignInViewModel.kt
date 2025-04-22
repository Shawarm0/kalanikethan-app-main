package com.lra.kalanikethencic.ui.screens.SignIn

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _allStudents = MutableStateFlow<List<Student>>(emptyList())
    private val _filteredStudents = MutableStateFlow<List<Student>>(emptyList())
    val filteredStudents: StateFlow<List<Student>> = _filteredStudents

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private var studentsJob: Job? = null
    private val pendingUpdates = mutableMapOf<Int, Student>()
    private var debounceJob: Job? = null


    fun preloadStudents() {
        if (_allStudents.value.isEmpty()) {
            loadStudents()
        }
    }

    private fun loadStudents() {
        studentsJob?.cancel()

        studentsJob = viewModelScope.launch {
            repository.getAllStudents()
                .catch { e ->
                    println("Error in student flow: ${e.message}")
                }
                .collect { studentList ->
                    _allStudents.value = studentList
                    filterStudents() // Filter when new data arrives
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            withContext(Dispatchers.Unconfined) {
                filterStudents()
            }
        }
    }

    fun filterStudents() {
        val query = _searchQuery.value.lowercase().trim()
        if (query.isEmpty()) {
            _filteredStudents.value = _allStudents.value.sortedBy { it.firstName.lowercase() }
        } else {
            _filteredStudents.value = _allStudents.value.filter { student ->
                student.firstName.lowercase().contains(query) == true ||
                        student.lastName.lowercase().contains(query) == true ||
                        student.studentId.toString().contains(query) == true
            }.sortedBy { it.firstName.lowercase() }
        }
    }

    fun updateStudent(updatedStudent: Student) {
        // Optimistically update both lists
        _allStudents.update { list ->
            list.map { if (it.studentId == updatedStudent.studentId) updatedStudent else it }
        }
        _filteredStudents.update { list ->
            list.map { if (it.studentId == updatedStudent.studentId) updatedStudent else it }
        }

        pendingUpdates[updatedStudent.studentId] = updatedStudent

        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(1000)
            val updatesToSend = pendingUpdates.values.toList()
            pendingUpdates.clear()
            updatesToSend.forEach {
                repository.updateStudent(it)
            }
        }
    }
}