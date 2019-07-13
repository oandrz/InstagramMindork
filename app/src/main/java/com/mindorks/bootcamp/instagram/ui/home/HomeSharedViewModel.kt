package com.mindorks.bootcamp.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class HomeSharedViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {
    override fun onCreate() {}

    val homeRedirection = MutableLiveData<Event<Boolean>>()
    val newPost: MutableLiveData<Event<Feed>> = MutableLiveData()

    fun onHomeRedirect() {
        homeRedirection.postValue(Event(true))
    }
}