package com.lra.kalanikethan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lra.kalanikethan.data.models.Class
import com.lra.kalanikethan.data.models.Student
import com.lra.kalanikethan.data.repository.ClassRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ClassManagementViewModel(
    private val classRepository: ClassRepository
) : ViewModel() {

    private val _allClasses = MutableStateFlow<List<Class>>(emptyList())
    val allClasses: StateFlow<List<Class>> = _allClasses

    private val _studentIdsByClass = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())
    val studentIdsByClass: StateFlow<Map<Int, Set<Int>>> = _studentIdsByClass

    private val _pendingStudentSelections = MutableStateFlow<Map<Int, Set<Int>>>(emptyMap())

    private val _allStudentsFlow = MutableStateFlow<List<Student>>(emptyList())

    private val _thisClass = MutableStateFlow<Class?>(null)
    val thisClass: MutableStateFlow<Class?> = _thisClass

    private val _isLoading = MutableStateFlow(false)
    val isLoading: MutableStateFlow<Boolean> = _isLoading

    val selectedStudentsForActiveClass: StateFlow<List<Student>> =
        combine(
            _studentIdsByClass,
            _pendingStudentSelections,
            _allStudentsFlow,
            _thisClass
        ) { idsByClass, pending, allStudents, activeClass ->
            val classId = activeClass?.classId ?: return@combine emptyList()
            val baseIds = idsByClass[classId].orEmpty()
            val pendingIds = pending[classId].orEmpty()
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
            _allClasses.value = classRepository.getAllClasses()
        }
    }

    fun bindAllStudents(studentsFlow: StateFlow<List<Student>>) {
        viewModelScope.launch {
            studentsFlow.collect { _allStudentsFlow.value = it }
        }
    }

    fun studentsForClass(classId: Int): StateFlow<List<Student>> =
        combine(
            _allStudentsFlow,
            _studentIdsByClass
        ) { allStudents, idsByClass ->
            val ids = idsByClass[classId].orEmpty()
            allStudents.filter { it.studentId in ids }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun loadStudentsForClass(classId: Int) {
        viewModelScope.launch {
            val studentIds = classRepository.getStudentIdsForClass(classId)
            _studentIdsByClass.update { current ->
                current + (classId to studentIds.toSet())
            }
        }
    }

    fun resetPendingUpdates() {
        _pendingStudentSelections.value = emptyMap()
    }

    fun updateClassState(classId: Int) {
        viewModelScope.launch {
            val pending = _pendingStudentSelections.value[classId].orEmpty()

            val studentsToAdd = pending.filter { it > 0 }
            val studentsToRemove = pending.filter { it < 0 }.map { -it }

            studentsToAdd.forEach { studentId ->
                classRepository.addStudentToClass(studentId = studentId, classId = classId)
            }

            studentsToRemove.forEach { studentId ->
                classRepository.removeStudentFromClass(studentId = studentId, classId = classId)
            }

            _pendingStudentSelections.update { map ->
                map.toMutableMap().apply { remove(classId) }
            }

            loadStudentsForClass(classId)
        }
    }

    fun toggleStudentSelection(classId: Int, studentId: Int, isSelected: Boolean) {
        _pendingStudentSelections.update { map ->
            val current = map[classId].orEmpty().toMutableSet()

            if (isSelected) {
                current.add(studentId)
                current.remove(-studentId)
            } else {
                current.add(-studentId)
                current.remove(studentId)
            }

            map.toMutableMap().apply { this[classId] = current }
        }
    }
}
