package com.mindorks.bootcamp.instagram.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class HomeViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val homeNavigation: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val profileNavigation: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val createNavigation: MutableLiveData<Event<Boolean>> = MutableLiveData()

    override fun onCreate() {
        homeNavigation.postValue(Event(true))
    }

    fun onHomeSelected() = homeNavigation.postValue(Event(true))

    fun onProfileSelected() = profileNavigation.postValue(Event(true))

    fun onCreateFeedSelected() = createNavigation.postValue(Event(true))
}