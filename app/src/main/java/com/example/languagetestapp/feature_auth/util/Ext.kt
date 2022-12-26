package com.example.languagetestapp.feature_auth.util

import com.example.languagetestapp.feature_auth.data.remote.model.TokenDto

fun TokenDto.countExp(): Long {
    val current = System.currentTimeMillis()
    val ms = this.accessTokenExp * 1000
    return current + ms
}