package com.wanjohi.david.demoapp.utils.network.vo

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import timber.log.Timber


data class Message(val title: String = "", val body: String = "") {
    var error = ErrorMessage(content = "Unknown error")
    fun parseError(gson: Gson) {
        try {
            error = gson.fromJson(body, ErrorMessage::class.java)
        } catch (e: JsonSyntaxException) {
            Timber.e(e)
            error.content = body
        }
    }
}
