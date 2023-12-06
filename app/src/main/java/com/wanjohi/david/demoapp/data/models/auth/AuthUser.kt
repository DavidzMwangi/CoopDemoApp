package com.wanjohi.david.demoapp.data.models.auth

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "auth_user_table")
data class AuthUser (

        @PrimaryKey
        @field:SerializedName("id")
        val id: Int,

        @field:SerializedName("firstName")
        val firstName: String,

        @field:SerializedName("lastName")
        val lastName: String,

        @field:SerializedName("username")
        val username: String,


        @field:SerializedName("email")
        val email: String,

        @field:SerializedName("gender")
        val gender: String,

        @field:SerializedName("image")
        val image: String,

        @field:SerializedName("token")
        val token: String,


)

