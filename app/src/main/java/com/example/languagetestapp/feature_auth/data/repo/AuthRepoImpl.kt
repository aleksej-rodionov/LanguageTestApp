package com.example.languagetestapp.feature_auth.data.repo

import com.example.languagetestapp.feature_auth.data.remote.LanguageAuthApi
import com.example.languagetestapp.feature_auth.domain.repo.AuthRepo

class AuthRepoImpl(
    private val authApi: LanguageAuthApi
): AuthRepo {


}