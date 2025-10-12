package com.lra.kalanikethan.ui.screens.dashBoardViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.models.StudentClass
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.ui.screens.signIn.SignInViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing dashboard operations including class and student data.
 *
 * @param repository The data repository for database operations
 * @param signInViewModel The ViewModel handling sign-in related operations
 */
class DashBoardViewModel (
    private val repository: Repository,
    private val signInViewModel: SignInViewModel,
) : ViewModel() {

    private val _allClasses = MutableStateFlow<List<Class>>(emptyList())
    // Flow emitting the list of all Classes
    val allClasses: StateFlow<List<Class>> = _allClasses

    // Local mutable state for pending changes to student selections
    private val _pendingStudentSelections = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())

    // Loading state indicator
    var isLoading = mutableStateOf(false)


    private val _class = mutableStateOf<Class>(Class(classId = 0, teacherName = "", type = "", startTime = "", endTime = ""))

    //Currently selected class state
    val thisClass: MutableState<Class> = _class

    /**
     * Toggles student selection for a specific class.
     *
     * @param classId The ID of the class to update
     * @param studentId The ID of the student to toggle
     * @param isSelected True if the student is being selected, false if being deselected
     */
    fun toggleStudentSelection(classId: Int, studentId: Int, isSelected: Boolean) {
        _pendingStudentSelections.update { currentMap ->
            val currentSelections = currentMap[classId] ?: emptySet()
            val newSelections = if (isSelected) {
                currentSelections + studentId
            } else {
                currentSelections - studentId
            }

            currentMap.toMutableMap().apply {
                this[classId] = newSelections
            }
        }

    }

    /**
     * Returns a flow of students for a specific class.
     * Combines student data from SignInViewModel with class information.
     *
     * @param classId The ID of the class to get students for
     * @return Flow emitting a list of students belonging to the specified class
     */
    fun getStudentsForClassFlow(classId: Int): Flow<List<Student>> {

        signInViewModel.initialiseStudentsChannel()

        return signInViewModel.allStudents.combine(allClasses) { students, classes ->
            // Get student IDs for this class
            val studentIds = getStudentIdsForClass(classId)

            // Filter students
            students.filter { student -> studentIds.contains(student.studentId) }
        }
    }

    /**
     * Retrieves student IDs for a specific class from the database.
     *
     * @param classId The ID of the class to query
     * @return List of student IDs belonging to the specified class
     */
    private suspend fun getStudentIdsForClass(classId: Int): List<Int> {
        return withContext(Dispatchers.IO) {
            client
                .from("student_classes").select(
                    columns = Columns.list("student_id", "class_id")
                ) {
                    filter {
                        Class::classId eq classId
                    }
                }.decodeList<StudentClass>()
                .map { it.studentId }
        }
    }

    /**
     * Resets all pending student selection updates.
     */
    fun resetPendingUpdates () {
        _pendingStudentSelections.value = emptyMap()
    }

    /**
     * Loads all classes from the repository into the state flow, [_allClasses].
     */
    fun loadClasses() {
        viewModelScope.launch {
            _allClasses.value = repository.getAllClasses()
        }
    }

    /**
     * Updates the class state in the repository with pending student selections.
     *
     * @param classId The ID of the class to update
     */
    fun updateClassState(classId: Int) {
        viewModelScope.launch {
            val pendingSelections = _pendingStudentSelections.value[classId] ?: emptySet()
            repository.updateClassState(classId, pendingSelections)
            repository.updateClass(thisClass.value)
        }
    }
}