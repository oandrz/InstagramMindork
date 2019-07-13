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

    val isRefresh: MutableLiveData<Boolean> = MutableLiveData()
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val refreshFeed: MutableLiveData<Resource<List<Feed>>> = MutableLiveData()
    private val feedLiveData: MutableLiveData<Resource<List<Feed>>> = MutableLiveData()

    fun getFeeds(): LiveData<List<Feed>> = Transformations.map(feedLiveData) { it.data }

    fun isFetchingFeed(): LiveData<Boolean> =
            Transformations.map(feedLiveData) { it.status == Status.LOADING }

    var firstId: String? = null
    var lastId: String? = null

    init {
        fetchFeed()
    }

    override fun onCreate() {
        feedLiveData.postValue(Resource.loading())
        loadMorePosts()
    }

    fun refresh() {
        firstId = null
        lastId = null
        feedLiveData.postValue(Resource.loading())
        isRefresh.postValue(true)
        feedDataSource.clear()
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

                        firstId = feedDataSource.maxBy { feed -> feed.createdAt.time }?.id
                        lastId = feedDataSource.minBy { feed -> feed.createdAt.time }?.id

                        loading.postValue(false)
                        feedLiveData.postValue(Resource.success(it))
                    },
                    {
                        handleNetworkError(it)
                        feedLiveData.postValue(Resource.error())
                    }
                )
        )
    }

    private fun loadMorePosts() {
        if (checkInternetConnectionWithMessage()) paginator.onNext(Pair(firstId, lastId))
    }

    fun onLoadMore() {
        if (loading.value != null && loading.value == false) loadMorePosts()
    }

    fun onNewPost(feed: Feed) {
        feedDataSource.add(0, feed)
        refreshFeed.postValue(Resource.success(mutableListOf<Feed>().apply { addAll(feedDataSource) }))
    }
}