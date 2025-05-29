package com.example.armoiraclefashionapp.api

import UserResponse
import com.example.armoiraclefashionapp.models.UserLogIn
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun logIn(@Body user: UserLogIn): UserResponse

}