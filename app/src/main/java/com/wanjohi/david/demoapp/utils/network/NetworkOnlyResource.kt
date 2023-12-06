package com.wanjohi.david.demoapp.utils.network

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.wanjohi.david.demoapp.utils.AppExecutors
import com.wanjohi.david.demoapp.utils.network.vo.*


abstract class NetworkOnlyResource<ResultType, RequestType> @MainThread constructor(
    private val appExecutors: AppExecutors,
    private val gson: Gson
) {

    private val result = MediatorLiveData<Resource<ResultType>>() //List<Repo>

    init {
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    @MainThread
    private fun setResultValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        val requestType = processResponse(response)
                        val resultType = processResult(requestType.body)
                        appExecutors.mainThread().execute {
                            setResultValue(Resource.success(resultType))
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.diskIO().execute {
                        appExecutors.mainThread().execute {
                            setResultValue(Resource.success(null))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    response.errorMessage.parseError(gson)
                    setResultValue(Resource.error(response.errorMessage, null))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response

    @WorkerThread
    protected abstract fun processResult(item: RequestType?): ResultType?

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}