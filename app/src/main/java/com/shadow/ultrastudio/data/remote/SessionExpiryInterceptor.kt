package com.shadow.ultrastudio.data.remote

import com.shadow.ultrastudio.data.repository.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class SessionExpiryInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        if (response.code == 401) {
            sessionManager.notifySessionExpired()
        }
        return response
    }
}
