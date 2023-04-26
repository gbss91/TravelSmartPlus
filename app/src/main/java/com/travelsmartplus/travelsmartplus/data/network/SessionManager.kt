package com.travelsmartplus.travelsmartplus.data.network

interface SessionManager {
    fun getToken(): String?
    fun saveToken(token: String)
    fun getRefreshToken(): String?
    fun saveRefreshToken(refreshToken: String)
    fun getUserId(): Int?
    fun saveUserId(userId: Int)
    fun clearSession()
}