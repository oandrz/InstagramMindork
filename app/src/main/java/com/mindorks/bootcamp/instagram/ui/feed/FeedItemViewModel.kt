package com.mindorks.bootcamp.instagram.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewModel
import com.mindorks.bootcamp.instagram.utils.common.Image
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class FeedItemViewModel @Inject constructor(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        feedRepository: FeedRepository
) : BaseItemViewModel<Feed>(schedulerProvider, compositeDisposable, networkHelper) {

    val user: LiveData<Avatar> = Transformations.map(data) { it.avatar }
    val image: LiveData<Image> =
            Transformations.map(data) { Image(it.imgUrl, it.imageWidth, it.imageHeight) }
    val likesCount: LiveData<Int> = Transformations.map(data) { it.likedBy.count() }
    val timeStamp: LiveData<String> = Transformations.map(data) { it.createdAt }


    override fun onCreate() {

    }
}