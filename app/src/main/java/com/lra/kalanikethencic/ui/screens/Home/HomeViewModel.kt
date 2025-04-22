package com.lra.kalanikethencic.ui.screens.Home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lra.kalanikethencic.data.model.Class
import com.lra.kalanikethencic.data.model.Student
import com.lra.kalanikethencic.data.model.StudentClassComposable
import com.lra.kalanikethencic.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _allClasses = MutableStateFlow<List<Class>>(emptyList())
    val allClasses: StateFlow<List<Class>> = _allClasses



    // Using full package name to avoid conflicts
    private val _classState = mutableStateOf<StudentClassComposable?>(null)
    val classState: State<StudentClassComposable?> = _classState


    private var debounceJob: Job? = null
    private val pendingUpdates = mutableMapOf<Int, Student>()

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading


    fun preloadClasses() {
        if (_allClasses.value.isEmpty()) {
            loadClasses()
        }
    }

    private fun loadClasses() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllClasses()
                    .collect { classList ->
                        _allClasses.value = classList
                    }
            }
        }
    }

    fun getClassById(classes: Class) {
        viewModelScope.launch {
            _isLoading.value = true
            _classState.value = null
            try {
                val students = repository.getStudentsForClass(classes.classId)
                _classState.value = StudentClassComposable(
                    classId = classes,
                    students = students
                )
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateStudent(updatedStudent: Student) {
        // Optimistically update the UI
        val currentClassState = _classState.value
        if (currentClassState != null) {
            val updatedStudents = currentClassState.students.map {
                if (it.studentId == updatedStudent.studentId) updatedStudent else it
            }
            _classState.value = currentClassState.copy(students = updatedStudents)
        }

        // Store the update for debouncing
        pendingUpdates[updatedStudent.studentId] = updatedStudent

        // Cancel any pending updates for this student
        debounceJob?.cancel()

        // Schedule a new update after 1 second of inactivity
        debounceJob = viewModelScope.launch {
            delay(1000)
            val updatesToSend = pendingUpdates.values.toList()
            pendingUpdates.clear()

            // Send all pending updates to repository
            withContext(Dispatchers.IO) {
                updatesToSend.forEach { student ->
                    repository.updateStudent(student)
                }
            }
        }
    }




}