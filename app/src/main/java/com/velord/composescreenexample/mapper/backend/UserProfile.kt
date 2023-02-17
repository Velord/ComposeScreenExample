package com.velord.composescreenexample.mapper.backend

import com.velord.backend.service.user.response.UserProfileResponse
import com.velord.model.profile.UserProfile

fun UserProfileResponse.toUserProfile() = UserProfile(
    id = id,
    name = "$firstName $lastName"
)