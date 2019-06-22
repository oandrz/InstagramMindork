package com.mindorks.bootcamp.instagram.ui.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.common.Status
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class FeedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val feedRepository: FeedRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val feedLiveData: MutableLiveData<Resource<List<Feed>>> = MutableLiveData()

    fun getFeeds(): LiveData<List<Feed>> = Transformations.map(feedLiveData) { it.data }

    fun isFetchingFeed(): LiveData<Boolean> =
            Transformations.map(feedLiveData) { it.status == Status.LOADING }

    override fun onCreate() {
        if (checkInternetConnectionWithMessage()) {
            feedLiveData.postValue(Resource.loading())
            compositeDisposable.add(
                    feedRepository.fetchPost()
                            .subscribeOn(schedulerProvider.io())
                            .subscribe(
                                    {
                                        feedLiveData.postValue(Resource.success(it))
                                    },
                                    {
                                        handleNetworkError(it)
                                        feedLiveData.postValue(Resource.error())
                                    }
                            )
            )
        }
    }
}