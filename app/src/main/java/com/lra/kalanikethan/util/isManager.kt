package com.lra.kalanikethan.util

import com.lra.kalanikethan.data.models.sessionPermissions


fun isManager(): Boolean {
    return sessionPermissions.value.manager
}