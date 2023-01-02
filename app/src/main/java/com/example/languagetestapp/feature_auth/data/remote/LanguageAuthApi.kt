package com.example.languagetestapp.feature_auth.data.remote

import com.example.languagetestapp.feature_auth.data.remote.model.*
import retrofit2.http.*

interface LanguageAuthApi {

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(@Field("email") email: String, @Field("password") password: String): AuthResponse<UserDto>

    @FormUrlEncoded
    @POST("/auth/login")
    suspend fun login(@Field("email") email: String, @Field("password") password: String): AuthResponse<TokenDto>

    @FormUrlEncoded
    @POST("/auth/refresh")
    suspend fun refresh(@Field("refreshtoken") refreshToken: String): AuthResponse<TokenDto>

    @FormUrlEncoded
    @POST("/auth/logout")
    suspend fun logout(@Field("refreshtoken") refreshToken: String): AuthResponse<String>
}