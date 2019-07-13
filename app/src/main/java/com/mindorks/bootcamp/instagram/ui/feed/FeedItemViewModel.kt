package com.mindorks.bootcamp.instagram.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewModel
import com.mindorks.bootcamp.instagram.utils.common.Image
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.common.TimeUtils
import com.mindorks.bootcamp.instagram.utils.display.ScreenUtils
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

class FeedItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val feedRepository: FeedRepository
) : BaseItemViewModel<Feed>(schedulerProvider, compositeDisposable, networkHelper) {

    private var isFetching: Boolean = false
    private val currentUser: User = userRepository.getCurrentUser()!!
    private val screenWidth: Int = ScreenUtils.getScreenWidth()
    private val screenHeight: Int = ScreenUtils.getScreenHeight()

    val userName: LiveData<String> = Transformations.map(data) { it.avatar.name }
    val profilePicture: LiveData<Image> = Transformations.map(data) {
        it.avatar.profilePictureUrl?.run { Image(this) }
    }
    val image: LiveData<Image> = Transformations.map(data) {
        Image(
            it.imgUrl,
            screenWidth,
            it.imageHeight?.let { height ->
                (calculateScaleFactor(it) * height).toInt()
            } ?: screenHeight / 3
        )
    }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.count() }
    val timeStamp: LiveData<String> = Transformations.map(data) { TimeUtils.getTimeAgo(it.createdAt) }
    val hasLiked: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy?.find { avatar -> avatar.id == currentUser.id } != null
    }

    val showFavouriteAnimation: MutableLiveData<ShowMode> = MutableLiveData()

    override fun onCreate() {

    }

    fun onFavouriteIconClicked() {
        if (!isFetching) {
            if (networkHelper.isNetworkConnected()) {
                if (hasLiked.value == true) {
                    data.value?.let {
                        unLikePost(it)
                        showFavouriteAnimation.postValue(ShowMode.REVERSE)
                    }
                } else {
                    data.value?.let {
                        likePost(it)
                        showFavouriteAnimation.postValue(ShowMode.STRAIGHT)
                    }
                }
            } else {
                messageStringId.postValue(Resource.error(R.string.network_connection_error))
            }
        }
    }

    private fun likePost(feed: Feed) {
        isFetching = true
        makeFavouriteRequest(feedRepository.likePost(feed, currentUser), feed.id)
    }

    private fun unLikePost(feed: Feed) {
        isFetching = true
        makeFavouriteRequest(feedRepository.unLikePost(feed, currentUser), feed.id)
    }

    private fun makeFavouriteRequest(request: Single<Feed>, feedId: String) {
        compositeDisposable.add(
            request
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    { responseFeed ->
                        isFetching = false
                        if (responseFeed.id == feedId) updateData(responseFeed)
                    },
                    { error ->
                        isFetching = false
                        handleNetworkError(error)
                    }
                )
        )
    }

    private fun calculateScaleFactor(feed: Feed): Float =
        feed.imageWidth?.let { screenWidth.toFloat() / it } ?: 1f
}

enum class ShowMode {
    REVERSE,
    STRAIGHT
}