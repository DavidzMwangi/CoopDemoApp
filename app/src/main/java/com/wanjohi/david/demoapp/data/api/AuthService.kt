package com.wanjohi.david.demoapp.data.api

import androidx.lifecycle.LiveData
import com.wanjohi.david.demoapp.utils.network.vo.ApiResponse
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import com.wanjohi.david.demoapp.data.models.auth.AuthUser
import retrofit2.http.Field

interface AuthService {

    @FormUrlEncoded
    @POST("auth/login")
    fun signIn(
        @Field("username") email: String,
        @Field("password") password: String,
    ): LiveData<ApiResponse<AuthUser>>
}