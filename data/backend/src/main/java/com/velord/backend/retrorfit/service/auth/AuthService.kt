package com.velord.backend.retrorfit.service.auth

interface AuthService {
    suspend fun login(username: String, password: String)
}

class AuthServiceImpl(
    private val retrofit: AuthRetrofitService
) : AuthService {

    override suspend fun login(username: String, password: String) {
        TODO()
    }
}