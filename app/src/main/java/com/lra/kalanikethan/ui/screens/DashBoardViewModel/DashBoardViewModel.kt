package com.lra.kalanikethan.ui.screens.DashBoardViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.models.StudentClass
import com.lra.kalanikethan.data.models.StudentClassComposable
import com.lra.kalanikethan.data.remote.SupabaseClientProvider.client
import com.lra.kalanikethan.data.repository.Repository
import com.lra.kalanikethan.ui.screens.SignIn.SignInViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class DashBoardViewModel (
    private val repository: Repository,
    private val signInViewModel: SignInViewModel,
) : ViewModel() {

    private val _allClasses = MutableStateFlow<List<Class>>(emptyList())
    val allClasses: StateFlow<List<Class>> = _allClasses

    // Using full package name to avoid conflicts
    private val _classState = mutableStateOf<StudentClassComposable?>(null)
    val classState: State<StudentClassComposable?> = _classState


    // Local mutable state for pending changes
    private val _pendingStudentSelections = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())




    var isLoading = mutableStateOf(false)

    private val _class = mutableStateOf<Class>(
        Class(
            classId = 0,
            teacherName = "",
            type = "",
            startTime = 0,
            endTime = 0
        )
    )
    val thisClass: MutableState<Class> = _class

    // Function to toggle student selection for a class
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

    fun resetPendingUpdates () {
        _pendingStudentSelections.value = emptyMap()
    }



    fun loadClasses() {
        viewModelScope.launch {
            _allClasses.value = repository.getAllClasses()
        }
    }

    fun updateClassState(classId: Int) {
        viewModelScope.launch {
            val pendingSelections = _pendingStudentSelections.value[classId] ?: emptySet()
            repository.updateClassState(classId, pendingSelections)
        }
    }


    fun getStudentsForClassFlow(classId: Int): Flow<List<Student>> {

        signInViewModel.initialiseStudentsChannel()

        return signInViewModel.allStudents.combine(allClasses) { students, classes ->
            // Get student IDs for this class
            val studentIds = getStudentIdsForClass(classId)

            // Filter students
            students.filter { student -> studentIds.contains(student.studentId) }
        }
    }



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
}

//@Serializable
//data class Class(
//    @SerialName("class_id") val classId: Int,
//    @SerialName("teacher_name") val teacherName: String,
//    @SerialName("type") val type: String,
//    @SerialName("start_time") val startTime: Long,
//    @SerialName("end_time") val endTime: Long
//)