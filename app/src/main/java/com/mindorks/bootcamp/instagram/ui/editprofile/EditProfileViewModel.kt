package com.mindorks.bootcamp.instagram.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bumptech.glide.load.model.GlideUrl
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.GlideHelper
import com.mindorks.bootcamp.instagram.utils.common.Status
import com.mindorks.bootcamp.instagram.utils.common.Validation
import com.mindorks.bootcamp.instagram.utils.common.Validator
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EditProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    @Inject
    lateinit var glideHeader: Map<String, String>

    private val validationList: MutableLiveData<List<Validation>> = MutableLiveData()
    private val profileLiveData: MutableLiveData<Avatar> = MutableLiveData()

    val email: MutableLiveData<String?> = MutableLiveData()
    val isAllFieldEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    override fun onCreate() {
    }

    fun setCurrentUser(currentUser: Avatar) {
        email.postValue(userRepository.getCurrentUser()?.email)
        profileLiveData.postValue(currentUser)
    }

    fun getCurrentUser(): LiveData<Avatar> = Transformations.map(profileLiveData) { it }

    fun getProfilePicture(): LiveData<GlideUrl> = Transformations.map(profileLiveData) {
        it.profilePictureUrl?.let { profPictureUrl ->
            GlideHelper.getProtectedUrl(profPictureUrl, glideHeader)
        }
    }

    fun updateProfileInformation(name: String, email: String, tagline: String) {
        val validations = Validator.validateUpdateProfileField(name, tagline, email)
        validationList.postValue(validations)

        if (validations.isNotEmpty()) {
            val errorValidations = validations.filter { it.resource.status == Status.ERROR }
            isAllFieldEmpty.postValue(errorValidations.size == validations.size)
        }
    }
}