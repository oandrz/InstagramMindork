package com.mindorks.bootcamp.instagram.ui.create

import androidx.lifecycle.MutableLiveData
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.data.repository.PhotoRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.FileUtils
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.io.File
import java.io.InputStream

class CreateViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository,
    private val photoRepository: PhotoRepository,
    private val feedRepository: FeedRepository,
    private val directory: File
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val post: MutableLiveData<Event<Feed>> = MutableLiveData()

    override fun onCreate() {

    }

    fun onCameraImageTaken(cameraImageProcessor: () -> String) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable { cameraImageProcessor() }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        File(it).apply {
                            FileUtils.getImageSize(this)?.let { size ->
                                uploadPhotoAndCreatePost(this, size)
                            } ?: loading.postValue(false)
                        }
                    },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.createPost_tryAgain_text))
                    }
                )
        )
    }

    fun onGalleryImageSelected(inputStream: InputStream) {
        loading.postValue(true)
        compositeDisposable.add(
            Single.fromCallable {
                FileUtils.saveInputStreamToFile(
                    inputStream, directory, "gallery_image_temp", 500
                )
            }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        if (it != null) {
                            FileUtils.getImageSize(it)?.run {
                                uploadPhotoAndCreatePost(it, this)
                            }
                        } else {
                            loading.postValue(false)
                            messageStringId.postValue(Resource.error(R.string.createPost_tryAgain_text))
                        }
                    },
                    {
                        loading.postValue(false)
                        messageStringId.postValue(Resource.error(R.string.createPost_tryAgain_text))
                    }
                )
        )
    }

    private fun uploadPhotoAndCreatePost(imageFile: File, imageSize: Pair<Int, Int>) {
        compositeDisposable.add(
            photoRepository.uploadPhoto(imageFile)
                .flatMap {
                    feedRepository.createPost(it, imageSize.first, imageSize.second)
                }
                .subscribeOn(schedulerProvider.io())
                .subscribe(
                    {
                        loading.postValue(false)
                        post.postValue(Event(it))
                    },
                    {
                        loading.postValue(false)
                        handleNetworkError(it)
                    }
                )
        )
    }
}