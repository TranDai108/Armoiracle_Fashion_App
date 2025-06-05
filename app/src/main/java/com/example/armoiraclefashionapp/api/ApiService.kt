package com.example.armoiraclefashionapp.api

import UserResponse
import com.example.armoiraclefashionapp.models.AnswerResponse
import com.example.armoiraclefashionapp.models.ClothesCreate
import com.example.armoiraclefashionapp.models.ClothesResponse
import com.example.armoiraclefashionapp.models.DeleteResponse
import com.example.armoiraclefashionapp.models.QuestionResponse
import com.example.armoiraclefashionapp.models.UserCreate
import com.example.armoiraclefashionapp.models.UserLogIn
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import com.example.armoiraclefashionapp.models.PersonalityResponse
import com.example.armoiraclefashionapp.models.UserClothesCreate
import com.example.armoiraclefashionapp.models.UserStyleCreate
import retrofit2.http.Query

interface ApiService {
    @POST("users/login")
    suspend fun logIn(@Body user: UserLogIn): UserResponse

    @GET("/users/single/{field}")
    suspend fun getUserBy(@Path("field") field: String, @Query("value") value: String): UserResponse

    @POST("users")
    suspend fun createUser(@Body user: UserCreate): UserResponse

    @POST("clothes")
    suspend fun createClothes(@Body clothes: ClothesCreate): ClothesResponse

    @Multipart
    @PATCH("users/{id}/avatar")
    suspend fun updateAvatar(@Path("id") id: String, @Part file: MultipartBody.Part ): UserResponse

    @Multipart
    @PATCH("clothes/{id}/picture")
    suspend fun updatePicture(@Path("id") id: String, @Part file: MultipartBody.Part ): ClothesResponse

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): DeleteResponse

    @GET("/questions")
    suspend fun getQuests(): List<QuestionResponse>

    @GET("/questions/{id}/answers")
    suspend fun getAnswers(@Path("id") id: String): List<AnswerResponse>

    @GET("answers/{id}/personalities")
    suspend fun getPersonal(@Path("id") id: String): List<PersonalityResponse>

    @GET("/users/{id}/clothes")
    suspend fun getAllClothesByUser(@Path("id") id: String): List<ClothesResponse>

    @GET("/users/{id}/recommend")
    suspend fun getRecommendClothesByUser(@Path("id") id: String): List<ClothesResponse>

    @POST("/user_styles")
    suspend fun createUserStyle(@Body body: UserStyleCreate): UserStyleCreate

    @GET("/personalities/name/{name}/styles")
    suspend fun getStylesByPersonality(@Path("name") name: String): List<String>

    @DELETE("clothes/{id}")
    suspend fun deleteClothes(@Path("id") id: String): DeleteResponse

    @POST("user_clothes")
    suspend fun createUserClothes(@Body body: UserClothesCreate): UserClothesCreate
}