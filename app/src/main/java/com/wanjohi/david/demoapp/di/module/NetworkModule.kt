package com.wanjohi.david.demoapp.di.module




import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wanjohi.david.demoapp.BuildConfig
import com.wanjohi.david.demoapp.utils.AppExecutors
import com.wanjohi.david.demoapp.utils.network.GsonExcludeStrategy
import com.wanjohi.david.demoapp.utils.network.LiveDataCallAdapterFactory

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
val networkModule = module {
    single{ AppExecutors() }
    single { RetrofitProvider(get(),get()) }
    single { providesGsonConverterFactory(get()) }
    single { providesHttpLoggingInterceptor() }
    single { providesGson() }
    single { GsonProvider() }
}




fun providesGsonConverterFactory(gson: GsonProvider): GsonConverterFactory {
    return GsonConverterFactory.create(gson.providesGson())
}


fun providesGson(): Gson {
    return GsonBuilder()
            .setExclusionStrategies(GsonExcludeStrategy())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create()
}




fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}


class RetrofitProvider(private val gsonConverterFactory: GsonConverterFactory,
                       private val  httpLoggingInterceptor: HttpLoggingInterceptor,
                       ){
    fun providesRetrofit(): Retrofit {
        val httpClient= OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .writeTimeout(2L, TimeUnit.MINUTES)
                .readTimeout(2L,TimeUnit.MINUTES)


        return Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
    }
}
class GsonProvider(){
    fun providesGson(): Gson {
        return GsonBuilder()
                .setExclusionStrategies(GsonExcludeStrategy())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .create()
    }
}


