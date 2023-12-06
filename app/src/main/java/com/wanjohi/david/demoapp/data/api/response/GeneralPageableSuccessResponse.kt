package com.wanjohi.david.demoapp.data.api.response

import com.google.gson.annotations.SerializedName
import com.wanjohi.david.demoapp.utils.network.paging.Pageable

data class GeneralPageableSuccessResponse<T>(

    @SerializedName("data")private var data: T?=null,
    @SerializedName("pagination")
    val pageable: Pageable?=null,

    ) {

    fun getResponseData(): T {
        return this.data as T
    }
}