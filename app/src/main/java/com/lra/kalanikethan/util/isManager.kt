package com.lra.kalanikethan.util

import com.lra.kalanikethan.data.models.sessionPermissions

/**
 * Checks if the current user is a manager.
 */
fun isManager(): Boolean {
    return sessionPermissions.value.manager
}