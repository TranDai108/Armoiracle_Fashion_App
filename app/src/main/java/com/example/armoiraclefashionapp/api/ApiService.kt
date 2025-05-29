package com.example.armoiraclefashionapp.api

import UserResponse
import com.example.armoiraclefashionapp.models.AnswerResponse
import com.example.armoiraclefashionapp.models.QuestionResponse
import com.example.armoiraclefashionapp.models.UserLogIn
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import com.example.armoiraclefashionapp.models.PersonalityResponse
interface ApiService {
    @POST("users/login")
    suspend fun logIn(@Body user: UserLogIn): UserResponse

    @GET("/questions")
    suspend fun getQuests(): List<QuestionResponse>

    @GET("/questions/{id}/answers")
    suspend fun getAnswers(@Path("id") id: String): List<AnswerResponse>

    @GET("answers/{id}/personalities")
    suspend fun getPersonal(@Path("id") id: String): List<PersonalityResponse>
}