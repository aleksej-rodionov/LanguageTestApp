package com.example.languagetestapp.feature_auth.data.remote

import com.example.languagetestapp.feature_auth.data.remote.model.AuthResponse
import com.example.languagetestapp.feature_auth.data.remote.model.LoginUserDto
import com.example.languagetestapp.feature_auth.data.remote.model.TokenDto
import com.example.languagetestapp.feature_auth.data.remote.model.UserDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LanguageAuthApi {

    @FormUrlEncoded
    @POST("/auth/register")
    suspend fun register(@Body user: UserDto): AuthResponse<UserDto>

    @POST("/auth/login")
    suspend fun login(@Body user: LoginUserDto): AuthResponse<TokenDto>

    @FormUrlEncoded
    @POST("/auth/refresh-token")
    suspend fun refreshToken(@Body refreshToken: String): AuthResponse<String>

    @FormUrlEncoded
    @DELETE("/auth/logout")
    suspend fun logout(@Body refreshToken: String): AuthResponse<String>
}