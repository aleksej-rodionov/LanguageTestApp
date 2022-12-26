package com.example.languagetestapp.feature_auth.data.remote

import com.example.languagetestapp.feature_auth.data.remote.model.*
import retrofit2.http.*

interface LanguageAuthApi {

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(@Body user: UserDto): AuthResponse<UserDto>

    @POST("/auth/login")
    suspend fun login(@Body user: LoginUserDto): AuthResponse<TokenDto>

//    @FormUrlEncoded
//    @POST("/auth/refresh")
//    suspend fun refresh(@Field("refreshtoken") refreshToken: String): AuthResponse<TokenDto>

    // todo use the above method after fixing urlencoded parser on the server
    @POST("/auth/refresh/{refreshtoken}")
    suspend fun refresh(@Path("refreshtoken") refreshtoken: String): AuthResponse<TokenDto>

//    @FormUrlEncoded
//    @DELETE("/auth/logout")
//    suspend fun logout(@Field("refreshtoken") refreshToken: String): AuthResponse<String>

    // todo use the above method after fixing urlencoded parser on the server
    @DELETE("/auth/logout/{refreshtoken}")
    suspend fun logout(@Path("refreshtoken") refreshToken: String): AuthResponse<String>
}