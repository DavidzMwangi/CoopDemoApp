package com.wanjohi.david.demoapp.di.module


import com.wanjohi.david.demoapp.data.api.AuthService
import org.koin.dsl.module


val apiModule = module {

    single { get<RetrofitProvider>().providesRetrofit().create(AuthService::class.java)}

}

