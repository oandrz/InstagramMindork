package com.mindorks.bootcamp.instagram.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewModel
import com.mindorks.bootcamp.instagram.utils.common.Image
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FeedItemViewModel @Inject constructor(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    userRepository: UserRepository,
    private val feedRepository: FeedRepository
) : BaseItemViewModel<Feed>(schedulerProvider, compositeDisposable, networkHelper) {

    private val currentUser: User = userRepository.getCurrentUser()!!
    private var isFetching: Boolean = false

    val user: LiveData<Avatar> = Transformations.map(data) { it.avatar }
    val image: LiveData<Image> =
        Transformations.map(data) { Image(it.imgUrl, it.imageWidth, it.imageHeight) }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy?.count() }
    val timeStamp: LiveData<String> = Transformations.map(data) { it.createdAt }
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
}

enum class ShowMode {
    REVERSE,
    STRAIGHT
}