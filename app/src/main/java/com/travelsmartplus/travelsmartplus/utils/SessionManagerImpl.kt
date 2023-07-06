package com.travelsmartplus.travelsmartplus.utils

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * SessionManagerImpl
 * Provides the implementation for managing user session data.
 *
 * @property sharedPreferences The SharedPreferences instance for storing session data.
 * @author Gabriel Salas
 */

@Singleton
class SessionManagerImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    SessionManager {

    companion object {
        private const val ACCESS_TOKEN = "token"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val USER_ID_COOKIE = "userId"
        private const val ORG_ID = "orgId"
        private const val SETUP = "isSetup"
    }

    override fun getToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, null)
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, token).apply()
    }

    override fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN, null)
    }

    override fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN, refreshToken).apply()
    }

    override fun currentUser(): Int? {
        return sharedPreferences.getInt(USER_ID_COOKIE, -1)
    }

    override fun saveCurrentUser(userId: Int) {
        sharedPreferences.edit().putInt(USER_ID_COOKIE, userId).apply()
    }

    override fun getOrgId(): Int? {
        return sharedPreferences.getInt(ORG_ID, -1)
    }

    override fun saveOrgId(orgId: Int) {
        sharedPreferences.edit().putInt(ORG_ID, orgId).apply()
    }

    override fun isSetup(): Boolean {
        return sharedPreferences.getBoolean(SETUP, false)
    }

    override fun saveSetup(isSetup: Boolean) {
        sharedPreferences.edit().putBoolean(SETUP, isSetup).apply()
    }

    override fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

}