package com.wanjohi.david.demoapp.utils.network.paging

import com.google.gson.annotations.SerializedName

data class Sort(

    @SerializedName("sorted") val sorted: Boolean,
    @SerializedName("unsorted") val unsorted: Boolean,
    @SerializedName("empty") val empty: Boolean
)