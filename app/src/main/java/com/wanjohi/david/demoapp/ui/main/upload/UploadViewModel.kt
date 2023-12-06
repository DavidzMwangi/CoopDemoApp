package com.wanjohi.david.demoapp.ui.main.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanjohi.david.demoapp.repository.TraceRepository
import com.wanjohi.david.demoapp.ui.main.login.states.UploadState
import com.wanjohi.david.demoapp.utils.network.vo.Status
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UploadViewModel(private val traceRepository: TraceRepository):ViewModel() {
    private var _uploadState = mutableStateOf(UploadState())
    var uploadState: State<UploadState> = _uploadState


    fun attemptLogin(username:String, password:String) {
        viewModelScope.launch {
            traceRepository.attemptLogin(username, password).collectLatest {
                when(it.status){
                    Status.EMPTY->{
                        _uploadState.value = uploadState.value.copy(
                            isLoading = false
                        )
                    }
                    Status.FAILED->{
                        _uploadState.value = uploadState.value.copy(
                            error= it.data?.error,
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
                            data  = it.data?.result!!
                        )
                    }
                }
            }
        }

    }
}