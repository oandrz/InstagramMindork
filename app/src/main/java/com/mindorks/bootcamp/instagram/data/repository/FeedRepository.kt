package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.request.LikeUnlikeBodyRequest
import io.reactivex.Single
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    userRepository: UserRepository
) {

    private var accessToken: String = userRepository.getCurrentUser()?.accessToken ?: ""
    private var userId: String = userRepository.getCurrentUser()?.id ?: ""

    fun fetchPost(firstPostId: String?, lastPostId: String?): Single<List<Feed>> =
        networkService
            .fetchFeed(firstPostId, lastPostId, userId, accessToken)
            .map { it.data }

    fun likePost(feed: Feed, currentUser: User): Single<Feed> =
        networkService
            .likeFeed(
                LikeUnlikeBodyRequest(feed.id),
                accessToken = accessToken,
                userId = userId
            )
            .map {
                feed.likedBy?.apply {
                    this.find { user -> user.id == currentUser.id } ?: this.add(
                        Avatar(
                            currentUser.id,
                            currentUser.name,
                            currentUser.profilePicUrl
                        )
                    )
                }
                feed
            }

    fun unLikePost(feed: Feed, currentUser: User): Single<Feed> =
        networkService
            .unLikeFeed(
                LikeUnlikeBodyRequest(feed.id),
                accessToken = accessToken,
                userId = userId
            )
            .map {
                feed.likedBy?.apply {
                    this.find { user -> user.id == currentUser.id }?.let { this.remove(it) }
                }
                feed
            }

    fun fetchSpecificPost(postId: String): Single<Feed> =
        networkService
            .fetchSpecificFeed(postId, accessToken = accessToken, userId = userId)
            .map { it.data }
}