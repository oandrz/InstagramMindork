package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val networkService: NetworkService,
    userRepository: UserRepository
) {

    private var accessToken: String = userRepository.getCurrentUser()?.accessToken ?: ""
    private var userId: String = userRepository.getCurrentUser()?.id ?: ""

    fun uploadPhoto(file: File): Single<String> {
        return MultipartBody.Part.createFormData(
            "image", file.name, RequestBody.create(MediaType.parse("image/*"), file)
        ).run {
            return@run networkService.doImageUpload(this, userId, accessToken)
                .map { it.data.imageUrl }
        }
    }
}