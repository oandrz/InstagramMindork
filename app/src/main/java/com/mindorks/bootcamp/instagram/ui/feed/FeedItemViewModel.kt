package com.mindorks.bootcamp.instagram.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewModel
import com.mindorks.bootcamp.instagram.utils.common.Image
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FeedItemViewModel @Inject constructor(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        private val feedRepository: FeedRepository
) : BaseItemViewModel<Feed>(schedulerProvider, compositeDisposable, networkHelper) {

    val user: LiveData<Avatar> = Transformations.map(data) { it.avatar }
    val image: LiveData<Image> =
            Transformations.map(data) { Image(it.imgUrl, it.imageWidth, it.imageHeight) }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy.count() }
    val timeStamp: LiveData<String> = Transformations.map(data) { it.createdAt }
    val hasLiked: LiveData<Boolean> = Transformations.map(data) {
        it.likedBy.map { feed -> feed.id }.contains(userRepository.getCurrentUser()?.id)
    }

    val showFavouriteAnimation: MutableLiveData<ShowMode> = MutableLiveData()

    private var isFetching: Boolean = false

    override fun onCreate() {

    }

    fun onFavouriteIconClicked() {
        if (!isFetching) {
            if (hasLiked.value == true) {
                data.value?.let {
                    unLikePost(it.id)
                    showFavouriteAnimation.postValue(ShowMode.REVERSE)
                }
            } else {
                data.value?.let {
                    likePost(it.id)
                    showFavouriteAnimation.postValue(ShowMode.STRAIGHT)
                }
            }
        }
    }

    private fun likePost(postId: String) {
        isFetching = true
        compositeDisposable.add(
                feedRepository.likePost(postId)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                                {
                                    updatePost(postId)
                                },
                                {
                                    handleNetworkError(it)
                                    isFetching = false
                                }
                        )
        )
    }

    private fun unLikePost(postId: String) {
        isFetching = true
        compositeDisposable.add(
                feedRepository.unLikePost(postId)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                                {
                                    updatePost(postId)
                                },
                                {
                                    handleNetworkError(it)
                                    isFetching = false
                                }
                        )
        )
    }

    private fun updatePost(postId: String) {
        compositeDisposable.add(
                feedRepository.fetchSpecificPost(postId)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                                {
                                    data.postValue(it)
                                    isFetching = false
                                },
                                {
                                    handleNetworkError(it)
                                    isFetching = false
                                }
                        )
        )
    }
}

enum class ShowMode {
    REVERSE,
    STRAIGHT
}