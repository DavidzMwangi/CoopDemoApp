package com.wanjohi.david.demoapp.di.module

import android.content.Context
import androidx.room.Room
import com.wanjohi.david.demoapp.data.dao.AuthUserDao
import com.wanjohi.david.demoapp.utils.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val databaseModule = module {
    single { provideDatabase(androidContext()) }
    single { provideAuthUserDao(get()) }
}

private fun provideDatabase(context:Context): AppDatabase {
   return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "demo_database"
        )
        .allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()
}




class DestroyDatabase(val database:AppDatabase){
    public fun destroyDatabase(){

        try {

            database.clearAllTables()
        }catch (e:Exception){ }
    }
}

private fun provideAuthUserDao(appDatabase: AppDatabase): AuthUserDao {
    return appDatabase.authUserDao()
}
