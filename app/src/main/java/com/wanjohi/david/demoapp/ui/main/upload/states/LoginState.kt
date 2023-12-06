package com.wanjohi.david.demoapp.ui.main.login.states

data class LoginState (
    val data:AuthUser = null,
    val isLoading: Boolean = false,
    val error: String? = null
)