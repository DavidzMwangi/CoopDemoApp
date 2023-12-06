package com.wanjohi.david.demoapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asFlow
import com.google.gson.Gson
import com.wanjohi.david.demoapp.data.api.AuthService
import com.wanjohi.david.demoapp.data.dao.AuthUserDao
import com.wanjohi.david.demoapp.data.models.auth.AuthUser
import com.wanjohi.david.demoapp.utils.AppExecutors
import com.wanjohi.david.demoapp.utils.network.NetworkOnlyResource
import com.wanjohi.david.demoapp.utils.network.vo.ApiResponse
import com.wanjohi.david.demoapp.utils.network.vo.ApiSuccessResponse
import com.wanjohi.david.demoapp.utils.network.vo.Resource
import kotlinx.coroutines.flow.Flow

class AuthRepository(
    private val authService: AuthService,
    val appExecutors: AppExecutors,
    val gson: Gson,
    val authUserDao: AuthUserDao
) {
    fun attemptLogin(username:String, password:String):Flow<Resource<AuthUser>>{
        return object : NetworkOnlyResource<AuthUser, AuthUser>(appExecutors,gson){
            override fun processResult(item: AuthUser?): AuthUser? {
                item?.let {
                    authUserDao.deleteAll()
                    authUserDao.insert(it)

                }
                return item
            }

            override fun createCall(): LiveData<ApiResponse<AuthUser>> {
                return  authService.signIn(username,password)
            }

            override fun processResponse(response: ApiSuccessResponse<AuthUser>): ApiSuccessResponse<AuthUser> {

                return super.processResponse(response)
            }

        }.asLiveData().asFlow()
    }


}
