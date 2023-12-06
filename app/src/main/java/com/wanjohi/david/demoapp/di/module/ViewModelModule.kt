package com.wanjohi.david.demoapp.di.module



import com.wanjohi.david.demoapp.ui.main.login.UploadViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{UploadViewModel(get())}
}