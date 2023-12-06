package com.wanjohi.david.demoapp.ui.main.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanjohi.david.demoapp.repository.AuthRepository
import com.wanjohi.david.demoapp.ui.main.login.states.LoginState
import com.wanjohi.david.demoapp.utils.network.vo.Status
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository):ViewModel() {
    private var _uploadState = mutableStateOf(LoginState())
    var uploadState: State<LoginState> = _uploadState


    fun attemptLogin(username:String, password:String) {
        viewModelScope.launch {
            authRepository.attemptLogin(username, password).collectLatest {
                when(it.status){
                    Status.EMPTY->{
                        _uploadState.value = uploadState.value.copy(
                            isLoading = false
                        )
                    }
                    Status.FAILED->{
                        _uploadState.value = uploadState.value.copy(
//                            error= it.data?.error,
                            isLoading = false
                        )
                    }
                    Status.LOADING->{
                        _uploadState.value = uploadState.value.copy(
                            isLoading = true
                        )
                    }
                    Status.SUCCESS->{
                        _uploadState.value = uploadState.value.copy(
                            isLoading = false,
                            data  = it.data
                        )
                    }
                }
            }
        }

    }
}