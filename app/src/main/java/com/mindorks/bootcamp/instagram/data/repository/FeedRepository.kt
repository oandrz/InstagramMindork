package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.request.CreateFeedRequest
import com.mindorks.bootcamp.instagram.data.remote.request.LikeUnlikeBodyRequest
import io.reactivex.Single
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    userRepository: UserRepository
) {

    private var accessToken: String = userRepository.getCurrentUser()?.accessToken ?: ""
    private var user: User = userRepository.getCurrentUser()!!
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
                            currentUser.profilePicUrl,
                            null,
                            0
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

    fun createPost(imgUrl: String, imgWidth: Int, imgHeight: Int): Single<Feed> =
        networkService.createFeed(
            CreateFeedRequest(imgUrl, imgWidth, imgHeight), userId = userId, accessToken = accessToken
        ).map {
            Feed(
                it.data.id,
                it.data.imageUrl,
                it.data.imageWidth,
                it.data.imageHeight,
                Avatar(user.id, user.name, user.profilePicUrl, null, 0),
                mutableListOf(),
                it.data.createdAt
            )
        }
}