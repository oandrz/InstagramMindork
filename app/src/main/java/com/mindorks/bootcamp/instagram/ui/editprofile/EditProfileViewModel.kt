package com.mindorks.bootcamp.instagram.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.bumptech.glide.load.model.GlideUrl
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.PhotoRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.log.Logger
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class EditProfileViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val directory: File
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private var glideHeader: Map<String, String> = mutableMapOf(
        (Networking.HEADER_USER_ID to (userRepository.getCurrentUser()?.id ?: "")),
        (Networking.HEADER_ACCESS_TOKEN to (userRepository.getCurrentUser()?.accessToken ?: "")),
        (Networking.HEADER_API_KEY to Networking.API_KEY)
    )

    private val validationList: MutableLiveData<List<Validation>> = MutableLiveData()
    private val profileLiveData: MutableLiveData<Avatar> = MutableLiveData()
    private val uploadPhotoLiveData: MutableLiveData<String?> = MutableLiveData()

    val launchHomeActivity: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
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

    fun getUploadedPhoto(): LiveData<GlideUrl> = Transformations.map(uploadPhotoLiveData) {
        it?.let { profPictureUrl ->
            GlideHelper.getProtectedUrl(profPictureUrl, glideHeader)
        }
    }

    fun onGalleryImageSelected(inputStream: InputStream) {
        compositeDisposable.add(
            Single.fromCallable {
                FileUtils.saveInputStreamToFile(
                    inputStream, directory, "gallery_img_profile", 500
                )
            }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        if (it != null) {
                            uploadPhoto(it)
                        } else {
                            ///TODO: Show error toast
                        }
                    },
                    {
                        ///TODO: Show error toast
                    }
                )
        )
    }

    fun updateProfileInformation(name: String, tagLine: String) {
        val validations = Validator.validateUpdateProfileField(name, tagLine)
        validationList.postValue(validations)

        if (validations.isNotEmpty()) {
            val errorValidations = validations.filter { it.resource.status == Status.ERROR }
            if (errorValidations.size != validations.size) {
                isAllFieldEmpty.postValue(false)
                if (checkInternetConnectionWithMessage()) {
                    val imageUrl: String = uploadPhotoLiveData.value ?: profileLiveData.value?.profilePictureUrl ?: ""
                    isLoading.postValue(true)
                    compositeDisposable.add(
                        userRepository.updateProfileInfo(name, tagLine, imageUrl)
                            .subscribeOn(schedulerProvider.io())
                            .subscribe(
                                {
                                    isLoading.postValue(false)
                                    launchHomeActivity.postValue(Event(emptyMap()))
                                },
                                {
                                    isLoading.postValue(false)
                                    handleNetworkError(it)
                                }
                            )
                    )
                }
            } else {
                isAllFieldEmpty.postValue(true)
            }
        }
    }

    private fun uploadPhoto(imageFile: File) {
        Logger.d("DEBUG", imageFile.path)
        if (checkInternetConnectionWithMessage()) {
            isLoading.postValue(true)
            compositeDisposable.add(
                photoRepository.uploadPhoto(imageFile)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            isLoading.postValue(false)
                            uploadPhotoLiveData.postValue(it)
                        },
                        {
                            isLoading.postValue(false)
                            handleNetworkError(it)
                        }
                    )
            )
        }
    }
}