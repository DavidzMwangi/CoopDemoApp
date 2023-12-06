package com.wanjohi.david.demoapp.utils.network.vo

import com.google.gson.annotations.SerializedName
import com.wanjohi.david.demoapp.utils.ERROR_MESSAGES
import java.util.*

data class ErrorMessage(
    val timestamp: Date = Date(),
    @field:SerializedName("httpStatus")
    val status: Int = 500,
    @field:SerializedName("developerMessage")
    val type: String = "unknown_error",
    @field:SerializedName("userMessage")
    var content: String?=null
) {
    fun getMessage(): String {
        return ERROR_MESSAGES[status] ?: "Oops! An error occurred"
    }
}
