package com.mindorks.bootcamp.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LikeUnlikeBodyRequest(
    @Expose
    @SerializedName("postId")
    val postId: String
)