package com.wanjohi.david.demoapp.utils.network.paging

import com.google.gson.annotations.SerializedName

data class Pageable(

    @SerializedName("last_visible_page") val lastVisiblePage: Int,
    @SerializedName("has_next_page") val hasNextPage: Boolean,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("items") val items: Items,


)
data class Items(
    @SerializedName("count") val count: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("per_page") val per_page: Int,

    )
