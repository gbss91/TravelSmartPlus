package com.travelsmartplus.travelsmartplus.utils

interface SessionManager {
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
}