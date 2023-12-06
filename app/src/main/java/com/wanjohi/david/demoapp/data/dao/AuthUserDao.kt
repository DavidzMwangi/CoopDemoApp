package com.wanjohi.david.demoapp.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.wanjohi.david.demoapp.data.models.auth.AuthUser
import androidx.room.Query

@Dao
interface AuthUserDao {
    @Query("SELECT * FROM auth_user_table LIMIT 1")
    fun getAuthUser():LiveData<AuthUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(authUser:AuthUser)

    @Query("DELETE FROM auth_user_table")
    fun deleteAll()

}