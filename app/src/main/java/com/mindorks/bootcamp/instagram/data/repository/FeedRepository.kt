package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.local.prefs.UserPreferences
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.request.LikeUnlikeBodyRequest
import com.mindorks.bootcamp.instagram.data.remote.response.GeneralResponse
import io.reactivex.Single
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    userPreferences: UserPreferences
) {

    private var accessToken: String = userPreferences.getAccessToken() ?: ""
    private var userId: String = userPreferences.getUserId() ?: ""

    fun fetchPost(): Single<List<Feed>> =
        networkService
            .fetchFeed(accessToken = accessToken, userId = userId)
            .map { it.data }

    fun likePost(postId: String): Single<GeneralResponse> =
        networkService
            .likeFeed(
                LikeUnlikeBodyRequest(postId), accessToken = accessToken, userId = userId
            )

    fun unLikePost(postId: String): Single<GeneralResponse> =
        networkService
            .unLikeFeed(
                LikeUnlikeBodyRequest(postId), accessToken = accessToken, userId = userId
            )
}