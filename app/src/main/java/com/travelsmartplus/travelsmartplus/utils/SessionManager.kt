package com.travelsmartplus.travelsmartplus.utils

import androidx.lifecycle.LiveData
import javax.inject.Singleton

@Singleton
interface SessionManager {
    val authenticationExpired: LiveData<Boolean>

    fun getToken(): String?

    fun saveToken(token: String)

    fun getRefreshToken(): String?

    fun saveRefreshToken(refreshToken: String)

    fun currentUser(): Int

    fun saveCurrentUser(userId: Int)

    fun admin(): Boolean

    fun saveAdmin(admin: Boolean)

    fun getOrgId(): Int

    fun saveOrgId(orgId: Int)

    fun isSetup(): Boolean

    fun saveSetup(isSetup: Boolean)

    fun clearSession()

    fun authenticationExpired()

    fun setAuthenticationExpired(boolean: Boolean)
}