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
import io.reactivex.processors.PublishProcessor

class FeedViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val feedRepository: FeedRepository,
    private val feedDataSource: ArrayList<Feed>,
    private val paginator: PublishProcessor<Pair<String?, String?>>
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val loading: MutableLiveData<Boolean> = MutableLiveData()
    private val feedLiveData: MutableLiveData<Resource<List<Feed>>> = MutableLiveData()

    fun getFeeds(): LiveData<List<Feed>> = Transformations.map(feedLiveData) { it.data }

    fun isFetchingFeed(): LiveData<Boolean> =
            Transformations.map(feedLiveData) { it.status == Status.LOADING }

    init {
        fetchFeed()
    }

    override fun onCreate() {
        loadMorePosts()
    }

    fun refresh() {
        loadMorePosts()
    }

    private fun fetchFeed() {
        ///TODO: Need to learn more about this
        compositeDisposable.add(
            paginator
                .onBackpressureDrop()
                .doOnNext {
                    loading.postValue(true)
                }
                .concatMapSingle { pageIds ->
                    return@concatMapSingle feedRepository
                        .fetchPost(pageIds.first, pageIds.second)
                        .subscribeOn(schedulerProvider.io())
                        .doOnError {
                            handleNetworkError(it)
                        }
                }
                .subscribe(
                    {
                        feedDataSource.addAll(it)
                        loading.postValue(false)
                        feedLiveData.postValue(Resource.success(it))
                    },
                    {
                        handleNetworkError(it)
                    }
                )
        )
    }

    private fun loadMorePosts() {
        val firstPostId = if (feedDataSource.isNotEmpty()) feedDataSource[0].id else null
        val lastPostId = if (feedDataSource.size > 1) feedDataSource[feedDataSource.size - 1].id else null
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstPostId, lastPostId))
    }

    fun onLoadMore() {
        if (loading.value != null && loading.value == false) loadMorePosts()
    }
}