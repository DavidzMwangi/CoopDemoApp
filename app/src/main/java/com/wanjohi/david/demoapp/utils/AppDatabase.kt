package com.wanjohi.david.demoapp.utils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wanjohi.david.demoapp.data.dao.AuthUserDao
import com.wanjohi.david.demoapp.data.models.auth.AuthUser

@Database(entities = [
    AuthUser::class,
],
    version = 1)
//@TypeConverters(Converters::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun authUserDao(): AuthUserDao

}