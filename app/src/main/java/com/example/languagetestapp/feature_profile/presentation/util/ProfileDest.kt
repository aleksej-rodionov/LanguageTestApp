package com.example.languagetestapp.feature_profile.presentation.util

import com.example.languagetestapp.feature_notes.presentation.util.NoteDest

sealed class ProfileDest(val route: String) {
    object ChangePasswordDest: ProfileDest(route = "changePassword")
    object ProfileDestination: ProfileDest(route = "profile")
    object CameraDest: ProfileDest(route = "camera")
    object PickImageDest: ProfileDest(route = "pickImage")
}
