package com.mindorks.bootcamp.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bumptech.glide.load.model.GlideUrl
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.remote.response.GeneralResponse
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.ui.editprofile.EditProfileActivity
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

    private var glideHeader: Map<String, String> = mutableMapOf(
        (Networking.HEADER_USER_ID to (userRepository.getCurrentUser()?.id ?: "")),
        (Networking.HEADER_ACCESS_TOKEN to (userRepository.getCurrentUser()?.accessToken ?: "")),
        (Networking.HEADER_API_KEY to Networking.API_KEY)
    )

    private val profileLiveData: MutableLiveData<Resource<Avatar>> = MutableLiveData()

    val launchEditProfile: MutableLiveData<Event<Map<String, Avatar>>> = MutableLiveData()
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

    fun handleEditProfileClicked() {
        profileLiveData.value?.data?.let {
            launchEditProfile.postValue(Event(mutableMapOf(
                Pair(EditProfileActivity.EXTRA_CURRENT_USER, it))))
        }
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