package com.wanjohi.david.demoapp.di.module


import com.wanjohi.david.demoapp.repository.AuthRepository
import org.koin.dsl.module

val repoModule = module {
    single { AuthRepository(get(),get(),get()) }
}