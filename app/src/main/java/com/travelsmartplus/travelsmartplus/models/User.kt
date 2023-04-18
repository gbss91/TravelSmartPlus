package com.travelsmartplus.travelsmartplus.models

data class User(
    val id: Int? = 0,
    val orgId: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val salt: String
)
