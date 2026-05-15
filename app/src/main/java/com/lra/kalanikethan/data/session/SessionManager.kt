package com.lra.kalanikethan.data.session

import com.lra.kalanikethan.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SessionManager {
    private val _currentUser = MutableStateFlow(User("None", "", false, ""))
    val currentUser: StateFlow<User> = _currentUser.asStateFlow()

    private val _authCompleted = MutableStateFlow(false)
    val authCompleted: StateFlow<Boolean> = _authCompleted.asStateFlow()

    val uid: String get() = _currentUser.value.uid
    val isManager: Boolean get() = _currentUser.value.manager

    fun setUser(user: User) {
        _currentUser.value = user
        _authCompleted.value = true
    }

    fun clearUser() {
        _currentUser.value = User("None", "", false, "")
        _authCompleted.value = false
    }
}
