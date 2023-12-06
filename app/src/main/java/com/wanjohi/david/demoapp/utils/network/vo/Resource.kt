package com.wanjohi.david.demoapp.utils.network.vo


class Resource<T>(var status: Status, var data: T?, var message: Message?) {

    companion object {
        fun <T> empty(data: T? = null): Resource<T> =
            Resource(Status.EMPTY, data, null)

        fun <T> success(data: T?): Resource<T> =
            Resource(Status.SUCCESS, data, null)

        fun <T> error(message: Message, data: T?): Resource<T> =
            Resource(Status.FAILED, data, message)

        fun <T> loading(data: T?): Resource<T> =
            Resource(Status.LOADING, data, null)
    }
}

