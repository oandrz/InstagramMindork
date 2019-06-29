package com.mindorks.bootcamp.instagram.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Avatar(
    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("name")
    val name: String,

    @Expose
    @SerializedName("profilePicUrl")
    val profilePictureUrl: String?,

    @Expose
    @SerializedName("tagline")
    val tagLine: String?,

    var postCount: Int
)