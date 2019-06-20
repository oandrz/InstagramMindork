package com.mindorks.bootcamp.instagram.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Feed(
    @Expose
    @SerializedName("id")
    val id: String,

    @Expose
    @SerializedName("imgUrl")
    val imgUrl: String,

    @Expose
    @SerializedName("imgWidth")
    val imageWidth: Int,

    @Expose
    @SerializedName("imgHeight")
    val imageHeight: Int,

    @Expose
    @SerializedName("user")
    val user: User,

    @Expose
    @SerializedName("likedBy")
    val likedBy: List<User>,

    @Expose
    @SerializedName("likedBy")
    val createdAt: String
)