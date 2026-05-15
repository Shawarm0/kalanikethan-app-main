package com.lra.kalanikethan.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.History
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.session.SessionManager
import com.lra.kalanikethan.data.remote.ChannelManager
import com.lra.kalanikethan.data.repository.AttendanceRepository
import com.lra.kalanikethan.data.repository.HistoryRepository
import com.lra.kalanikethan.data.repository.StudentRepository
import com.lra.kalanikethan.util.Tables
import com.lra.kalanikethan.util.Timing
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.postgresChangeFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate

class AttendanceViewModel(
    private val studentRepository: StudentRepository,
    private val attendanceRepository: AttendanceRepository,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    private val _allStudents = MutableStateFlow<List<Student>>(emptyList())
    val allStudents: StateFlow<List<Student>> = _allStudents

    private val _displayedStudents = MutableStateFlow<List<Student>>(emptyList())
    val displayedStudents: StateFlow<List<Student>> = _displayedStudents

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _histories = MutableStateFlow<List<History>>(emptyList())

    private val signInDebounceMap = mutableMapOf<Int?, Job>()

    init {
        viewModelScope.launch {
            _allStudents.value = studentRepository.getAllStudents()
            _histories.value = historyRepository.getAllHistories()
            filterStudents()
            subscribeToStudentChanges()
        }
    }

    private fun subscribeToStudentChanges() {
        viewModelScope.launch {
            val channel = ChannelManager.studentsChannel()
            val changes = channel.postgresChangeFlow<PostgresAction>(schema = "public") {
                table = Tables.STUDENTS
            }
            channel.subscribe()

            changes.collect { action ->
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
                        val deletedId = action.oldRecord["student_id"]?.jsonPrimitive?.intOrNull
                        if (deletedId != null) {
                            _allStudents.update { list -> list.filter { it.studentId != deletedId } }
                            filterStudents()
                        }
                    }
                    else -> {
                        Log.i("StudentSync", "Unknown action: $action")
                    }
                }
            }
        }
    }

    fun removeSearchQuery() {
        _searchQuery.value = ""
        filterStudents()
    }

    fun updateStudentAttendance(updatedStudent: Student, currentSignInStatus: Boolean, classId: Int?) {
        Log.i("Database-Attendance", "Student: $updatedStudent, Action: ${if (currentSignInStatus) "SignIn" else "SignOut"}")

        _allStudents.update { list ->
            list.map { if (it.studentId == updatedStudent.studentId) updatedStudent else it }
        }
        filterStudents()

        signInDebounceMap[updatedStudent.studentId]?.cancel()

        val job = viewModelScope.launch {
            delay(Timing.ATTENDANCE_DEBOUNCE_MS)

            if (currentSignInStatus) {
                val history = _histories.value.find {
                    it.studentID == updatedStudent.studentId && it.signOutTime == null
                }
                Log.i("History-SignOut", "History: $history")
                attendanceRepository.signOutStudent(updatedStudent, history, SessionManager.uid)
                delay(Timing.POST_DB_SETTLE_MS)
                syncHistories()
            } else {
                val history = History(
                    studentID = updatedStudent.studentId,
                    classID = classId,
                    date = "${LocalDate.now().year}-${LocalDate.now().month}-${LocalDate.now().dayOfMonth}",
                    signInTime = System.currentTimeMillis(),
                    signOutTime = null,
                    uid = SessionManager.uid
                )
                Log.i("History-SignIn", "History: $history")
                attendanceRepository.signInStudent(updatedStudent, history)
                delay(Timing.POST_DB_SETTLE_MS)
                syncHistories()
            }
            signInDebounceMap.remove(updatedStudent.studentId)
        }
        signInDebounceMap[updatedStudent.studentId] = job
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        filterStudents()
    }

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

    private fun syncHistories() {
        viewModelScope.launch {
            try {
                _histories.value = historyRepository.getAllHistories()
            } catch (e: Exception) {
                Log.e("HistorySync", "Error syncing histories: ${e.message}")
            }
        }
    }
}
