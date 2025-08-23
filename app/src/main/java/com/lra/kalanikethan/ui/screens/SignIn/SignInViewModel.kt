package com.lra.kalanikethan.ui.screens.SignIn

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.remote.ChannelManager
import com.lra.kalanikethan.data.repository.Repository
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * ViewModel for managing student sign-in data and search functionality.
 *
 * Responsibilities:
 * - Fetching all students from the repository.
 * - Filtering students based on a search query.
 * - Subscribing to real-time updates via Supabase channels.
 *
 * @param repository Data source for student information.
 */
class SignInViewModel(
    private val repository: Repository
) : ViewModel() {

    //Holds the complete list of students retrieved from the repository.
    private val _allStudents = MutableStateFlow<List<Student>>(emptyList())
    val allStudents: StateFlow<List<Student>> = _allStudents

    // Holds the filtered list of students currently displayed in the UI.
    private val _displayedStudents = MutableStateFlow<List<Student>>(emptyList())
    val displayedStudents: StateFlow<List<Student>> = _displayedStudents

    // Holds the current search query entered by the user.
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val signInDebounceJobs = mutableMapOf<Int?, Job>()
    private var debounceJob: Job? = null

    /**
     * Loads all students from the repository and applies the initial filter.
     */
    fun initializeStudents() {
        viewModelScope.launch {
            _allStudents.value = repository.getAllStudents()
            filterStudents()
        }
    }

    /**
     * Removes the current search query.
     */
    fun removeSearchQuery() {
        _searchQuery.value = ""
        filterStudents()
    }

    /**
     * Retrieves the complete list of students.
     *
     * @return A [StateFlow] containing the list of students.
     */
    fun getAllStudentsFlow(): StateFlow<List<Student>> = allStudents

    fun signIn(updatedStudent: Student) {
        Log.i("Database-SignIn", "Student: $updatedStudent")

        // Always update UI immediately
        _allStudents.update { list ->
            list.map { if (it.studentId == updatedStudent.studentId) updatedStudent else it }
        }
        filterStudents()

        // Cancel any pending DB update for this student
        signInDebounceJobs[updatedStudent.studentId]?.cancel()

        // Schedule a new DB update after 1 second of inactivity
        val job = viewModelScope.launch {
            delay(1000) // wait to see if spam stops
            repository.updateStudent(updatedStudent) // update DB once
            signInDebounceJobs.remove(updatedStudent.studentId) // cleanup
        }

        signInDebounceJobs[updatedStudent.studentId] = job
    }



    /**
     * Updates the current search query and triggers filtering.
     *
     * @param query The new search term entered by the user.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            withContext(Dispatchers.Unconfined) {
                filterStudents()
            }
        }
    }

    /**
     * Filters the list of students based on the current [searchQuery].
     *
     * Matching is done against:
     * - First name (case-insensitive)
     * - Last name (case-insensitive)
     * - Exact student ID
     */
    fun filterStudents() {
        val query = _searchQuery.value.lowercase().trim()
        if (query.isEmpty()) {
            _displayedStudents.value =
                _allStudents.value.sortedBy { it.firstName.lowercase() }
        } else {
            _displayedStudents.value = _allStudents.value.filter { student ->
                student.firstName.lowercase().contains(query) ||
                        student.lastName.lowercase().contains(query) ||
                        student.studentId.toString() == query
            }.sortedBy { it.firstName.lowercase() }
        }
    }

    /**
     * Subscribes to the real-time Supabase students channel.
     *
     * Handles updates by replacing the affected student in [_allStudents]
     * and reapplying the current filter.
     *
     * Note: Don't need to worry about unsubscription to a realTime Channel as that is done in ChannelManager
     */
    fun initialiseStudentsChannel() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            val channel = ChannelManager.subscribeToStudentsChannel()

            channel.collect { action ->
                when (action) {
                    is PostgresAction.Delete -> TODO()
                    is PostgresAction.Insert -> TODO()
                    is PostgresAction.Select -> TODO()
                    is PostgresAction.Update -> {
                        val student = Json.decodeFromJsonElement<Student>(action.record)
                        _allStudents.update { list ->
                            list.map { if (it.studentId == student.studentId) student else it }
                        }
                        filterStudents()
                    }
                }
            }
        }
    }
}
