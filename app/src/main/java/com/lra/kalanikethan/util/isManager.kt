package com.lra.kalanikethan.util

import com.lra.kalanikethan.data.session.SessionManager

fun isManager(): Boolean {
    return SessionManager.isManager
}