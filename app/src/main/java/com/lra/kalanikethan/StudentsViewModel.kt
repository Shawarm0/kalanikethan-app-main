package com.lra.kalanikethan

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.data.models.Class
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Employee
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.sessionPermissions
import com.lra.kalanikethan.data.remote.ChannelManager
import io.github.jan.supabase.realtime.PostgresAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import java.time.LocalDate
import kotlin.collections.set

class StudentsViewModel(
    private val repository: Repository
) : ViewModel() {
    // From signInViewmodel
    //Holds the complete list of students retrieved from the repository.
    private val _allStudents = MutableStateFlow<List<Student>>(emptyList())
    val allStudents: StateFlow<List<Student>> = _allStudents

    private val _allEmployees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _allEmployees

    private val _allHistories = MutableStateFlow<List<History>>(emptyList())
    val histories: StateFlow<List<History>> = _allHistories


    // Holds the filtered list of students currently displayed in the UI.
    private val _displayedStudents = MutableStateFlow<List<Student>>(emptyList())
    val displayedStudents: StateFlow<List<Student>> = _displayedStudents

    // Holds the current search query entered by the user.
    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val signInDebounceMap = mutableMapOf<Int?, Job>()
    private var signInDebounceJob: Job? = null
    private var historyDebounceJob: Job? = null





    // From dashBoardViewModel
    private val _allClasses = MutableStateFlow<List<Class>>(emptyList())
    val allClasses: StateFlow<List<Class>> = _allClasses

    private val _studentsByClass =
        MutableStateFlow<Map<Int, List<Student>>>(emptyMap())

    val studentsByClass: StateFlow<Map<Int, List<Student>>> = _studentsByClass


    val thisClass = mutableStateOf<Class?>(null)

    private val _pendingStudentSelections =
        MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())

    val isLoading = mutableStateOf(false)


    val selectedStudentsForActiveClass: StateFlow<List<Student>> =
        combine(
            studentsByClass,
            _pendingStudentSelections,
            allStudents
        ) { byClass, pending, allStudents ->

            val classId = thisClass.value?.classId ?: return@combine emptyList()

            val baseStudents = byClass[classId].orEmpty()
            val pendingIds = pending[classId].orEmpty()

            val baseIds = baseStudents.mapNotNull { it.studentId }.toSet()

            val finalIds =
                baseIds
                    .minus(pendingIds.filter { it < 0 }.map { -it }.toSet())
                    .plus(pendingIds.filter { it > 0 }.toSet())

            allStudents.filter { it.studentId in finalIds }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )




    init {
        viewModelScope.launch {
            _allStudents.value = repository.getAllStudents()
            _allClasses.value = repository.getAllClasses()
            _allEmployees.value = repository.getAllEmployees()
            _allHistories.value = repository.getAllHistories()
            initialiseStudentsChannel()
//            initialiseHistoryChannel()
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
//    fun getAllStudentsFlow(): StateFlow<List<Student>> = allStudents

    /**
     * Updates student attendance with debounced database operations.
     *
     * Immediately updates UI state, then schedules a database update after a delay
     * to prevent spam. Cancels any pending updates for the same student.
     *
     * @param updatedStudent The student with updated attendance status
     * @param currentSignInStatus True if signing in, false if signing out
     */
    fun updateStudentAttendance(updatedStudent: Student, currentSignInStatus: Boolean) {
        Log.i("Database-Attendance", "Student: $updatedStudent, Action: ${if (currentSignInStatus) "SignIn" else "SignOut"}")

        // Always update UI immediately
        _allStudents.update { list ->
            list.map { if (it.studentId == updatedStudent.studentId) updatedStudent else it }
        }
        filterStudents()


        // Cancel any pending DB update for this student
        signInDebounceMap[updatedStudent.studentId]?.cancel()

        // Schedule a new DB update after 1 second of inactivity
        val job = viewModelScope.launch {
            delay(1000) // wait to see if spam stops

            if (currentSignInStatus) {
                // Find the history from the current _allHistories value (not activeSignIns)
                val history = _allHistories.value.find {
                    it.studentID == updatedStudent.studentId && it.signOutTime == null
                }
                Log.i("History-SignOut", "History: $history")
                repository.signOutStudent(updatedStudent, history) // Handle sign-out specifically
                delay(10)
                syncAllHistories()
            } else {

                val history = History(
                    studentID = updatedStudent.studentId,
                    date = "${LocalDate.now().year}-${LocalDate.now().month}-${LocalDate.now().dayOfMonth}",
                    signInTime = System.currentTimeMillis(),
                    signOutTime = null,
                    uid = sessionPermissions.value.uid
                )
                Log.i("History-SignIn", "History: $history")
                repository.signInStudent(updatedStudent, history) // Handle sign-in specifically
                delay(10)
                syncAllHistories()
            }
            signInDebounceMap.remove(updatedStudent.studentId) // cleanup
        }
        signInDebounceMap[updatedStudent.studentId] = job
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
     * Filters the list of students based on the current [searchQue ry].
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
    @Suppress("SpellCheckingInspection")
    fun initialiseStudentsChannel() {
        signInDebounceJob?.cancel()
        signInDebounceJob = viewModelScope.launch {
            val channel = ChannelManager.subscribeToStudentsChannel()

            channel.collect { action ->
                when (action) {
                    is PostgresAction.Insert -> {
                        val student = Json.decodeFromJsonElement<Student>(action.record)
                        _allStudents.update { list -> list + student }
                        filterStudents()
                    }
                    is PostgresAction.Update -> {
                        val student = Json.decodeFromJsonElement<Student>(action.record)
                        _allStudents.update { list ->
                            list.map { if (it.studentId == student.studentId) student else it }
                        }
                        filterStudents()
                    }
                    is PostgresAction.Delete -> {
                        val student = Json.decodeFromJsonElement<Student>(action.oldRecord)
                        _allStudents.update { list -> list - student }
                        filterStudents()
                    }
                    else -> {
                        Log.i("StudentSync-SignInViewModel", "Unknown action: $action")
                    }
                }
            }
        }
    }

    /**
     * Subscribes to the real-time Supabase students channel.
     *
     * Handles updates by replacing the affected student in [_allHistories]
     * and reapplying the current filter.
     *
     * Note: Don't need to worry about unsubscription to a realTime Channel as that is done in ChannelManager
     */
    fun initialiseHistoryChannel() {
        historyDebounceJob?.cancel()
        historyDebounceJob = viewModelScope.launch {
            val channel = ChannelManager.subscribeToHistoryChannel()

            channel.collect { action ->
                when (action) {
                    is PostgresAction.Insert -> {
                        val history = Json.decodeFromJsonElement<History>(action.record)
                        _allHistories.update { list -> list + history }
                    }
                    is PostgresAction.Update -> {
                        val history = Json.decodeFromJsonElement<History>(action.record)
                        _allHistories.update { list ->
                            list.map { if (it.historyID == history.historyID) history else it }
                        }
                    }
                    is PostgresAction.Delete -> {
                        val history = Json.decodeFromJsonElement<History>(action.oldRecord)
                        _allHistories.update { list -> list - history }
                    }
                    else -> {
                        Log.i("HistorySync-SignInViewModel", "Unknown action: $action")
                    }
                }
            }
        }
    }

    /**
     * Syncs all histories from the repository to the local state.
     * Handles any exceptions during the sync process.
     */
    private fun syncAllHistories() {
        viewModelScope.launch {
            try {
                val allHistories = repository.getAllHistories()
                _allHistories.value = allHistories
            } catch (e: Exception) {
                Log.e("HistorySync", "Error syncing all histories: ${e.message}")
            }
        }
    }



    fun loadStudentsForClass(classId: Int) {
        viewModelScope.launch {
            val studentIds = repository.getStudentIdsForClass(classId)

            _studentsByClass.update { current ->
                current + (classId to _allStudents.value.filter {
                    it.studentId in studentIds
                })
            }
        }
    }


    /**
     * Resets all pending student selection updates.
     */
    fun resetPendingUpdates () {
        _pendingStudentSelections.value = emptyMap()
    }


    /**
     * Updates the class state in the repository with pending student selections.
     *
     * @param classId The ID of the class to update
     */
    fun updateClassState(classId: Int) {
        viewModelScope.launch {
            val pending = _pendingStudentSelections.value[classId].orEmpty()

            val studentsToAdd = pending.filter { it > 0 }
            val studentsToRemove = pending.filter { it < 0 }.map { -it }

            // ADD students
            studentsToAdd.forEach { studentId ->
                repository.addStudentToClass(
                    studentId = studentId,
                    classId = classId
                )
            }

            // REMOVE students
            studentsToRemove.forEach { studentId ->
                repository.removeStudentFromClass(
                    studentId = studentId,
                    classId = classId
                )
            }

            // Clear pending state after successful save
            _pendingStudentSelections.update { map ->
                map.toMutableMap().apply {
                    remove(classId)
                }
            }

            // Reload class students from DB
            loadStudentsForClass(classId)
        }
    }


    /**
     * Toggles student selection for a specific class.
     *
     * @param classId The ID of the class to update
     * @param studentId The ID of the student to toggle
     * @param isSelected True if the student is being selected, false if being deselected
     */
    fun toggleStudentSelection(classId: Int, studentId: Int, isSelected: Boolean) {
        _pendingStudentSelections.update { map ->
            val current = map[classId].orEmpty().toMutableSet()

            if (isSelected) {
                current.add(studentId)      // add
                current.remove(-studentId)  // undo removal
            } else {
                current.add(-studentId)     // remove
                current.remove(studentId)   // undo add
            }

            map.toMutableMap().apply {
                this[classId] = current
            }
        }
    }


}