package com.mindorks.bootcamp.instagram.data.remote.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateFeedRequest(
    @Expose
    @SerializedName("imgUrl")
    var imageUrl: String,

    @Expose
    @SerializedName("imgWidth")
    var imageWidth: Int,

    @Expose
    @SerializedName("imgHeight")
    var imageHeight: Int
)