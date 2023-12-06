package com.wanjohi.david.demoapp.ui.main.login.states

import com.wanjohi.david.demoapp.data.models.auth.AuthUser

data class LoginState (
    val data: AuthUser?=null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success:Boolean= false
)