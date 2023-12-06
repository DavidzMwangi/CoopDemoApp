package com.wanjohi.david.demoapp.ui.main.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanjohi.david.demoapp.repository.AuthRepository
import com.wanjohi.david.demoapp.ui.main.login.states.LoginState
import com.wanjohi.david.demoapp.utils.network.vo.Status
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository):ViewModel() {
    private var _loginState = mutableStateOf(LoginState())
    var loginState: State<LoginState> = _loginState
    private val _page = MutableLiveData(Page.LOGIN)
    val page: LiveData<Page> = _page

    val changePage: (Page) -> Unit = {
        _page.value = it
    }
    fun attemptLogin(username:String, password:String) {
        viewModelScope.launch {
            authRepository.attemptLogin(username, password).collectLatest {
                when(it.status){
                    Status.EMPTY->{
                        _loginState.value = loginState.value.copy(
                            isLoading = false
                        )
                    }
                    Status.FAILED->{
                        _loginState.value = loginState.value.copy(
                            error= "Invalid Credentials",
                            isLoading = false
                        )
                    }
                    Status.LOADING->{
                        _loginState.value = loginState.value.copy(
                            isLoading = true
                        )
                    }
                    Status.SUCCESS->{
                        _loginState.value = loginState.value.copy(
                            isLoading = false,
                            data  = it.data,
                            success = true
                        )
                    }
                }
            }
        }

    }
}
enum class Page { LOGIN, WELCOME }