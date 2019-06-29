package com.mindorks.bootcamp.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bumptech.glide.load.model.GlideUrl
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.remote.response.GeneralResponse
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.GlideHelper
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class ProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    @Inject
    lateinit var glideHeader: Map<String, String>

    private val profileLiveData: MutableLiveData<Resource<Avatar>> = MutableLiveData()

    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun getProfilePicture(): LiveData<GlideUrl> = Transformations.map(profileLiveData) {
        it.data?.profilePictureUrl?.let { profPictureUrl ->
            GlideHelper.getProtectedUrl(profPictureUrl, glideHeader)
        }
    }

    fun getMyProfile(): LiveData<Avatar> = Transformations.map(profileLiveData) { it.data }

    fun logout() {
        isLoading.postValue(true)
        compositeDisposable.add(
            userRepository.logout()
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        isLoading.postValue(false)
                        launchLogin.postValue(Event(emptyMap()))
                    },
                    {
                        isLoading.postValue(false)
                        handleNetworkError(it)
                    }
                )
        )
    }

    override fun onCreate() {
        fetchMyProfile()
    }

    private fun fetchMyProfile() {
        isLoading.postValue(true)
        compositeDisposable.add(
            userRepository.fetchMyProfile()
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        isLoading.postValue(false)
                        profileLiveData.postValue(Resource.success(it))
                    },
                    {
                        isLoading.postValue(false)
                        handleNetworkError(it)
                        profileLiveData.postValue(Resource.error())
                    }
                )
        )
    }
}