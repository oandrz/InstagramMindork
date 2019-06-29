package com.mindorks.bootcamp.instagram.data.repository

import com.mindorks.bootcamp.instagram.data.local.db.DatabaseService
import com.mindorks.bootcamp.instagram.data.local.prefs.UserPreferences
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.remote.NetworkService
import com.mindorks.bootcamp.instagram.data.remote.request.LoginRequest
import com.mindorks.bootcamp.instagram.data.remote.request.SignupRequest
import com.mindorks.bootcamp.instagram.data.remote.response.MyPostListResponse
import com.mindorks.bootcamp.instagram.data.remote.response.MyProfileResponse
import com.mindorks.bootcamp.instagram.data.remote.response.UserResponse
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val networkService: NetworkService,
    private val databaseService: DatabaseService,
    private val userPreferences: UserPreferences
) {

    fun saveCurrentUser(user: User) {
        userPreferences.setUserId(user.id)
        userPreferences.setUserName(user.name)
        userPreferences.setUserEmail(user.email)
        userPreferences.setAccessToken(user.accessToken)
    }

    fun removeCurrentUser() {
        userPreferences.removeUserId()
        userPreferences.removeUserName()
        userPreferences.removeUserEmail()
        userPreferences.removeAccessToken()
    }

    fun getCurrentUser(): User? {
        val userId = userPreferences.getUserId()
        val userName = userPreferences.getUserName()
        val userEmail = userPreferences.getUserEmail()
        val accessToken = userPreferences.getAccessToken()

        return if (userId !== null && userName != null && userEmail != null && accessToken != null)
            User(userId, userName, userEmail, accessToken)
        else
            null
    }

    fun doLogin(email: String, password: String): Single<User> =
        networkService
            .doLoginCall(LoginRequest(email, password))
            .map { mapUserResponseAndSaveIntoPref(it) }

    fun doSignUp(email: String, password: String, name: String): Single<User> =
        networkService
            .doSignUpCall(SignupRequest(email, password, name))
            .map { mapUserResponseAndSaveIntoPref(it) }

    fun fetchMyProfile(): Single<Avatar> =
        Single.zip(
            networkService.fetchMyProfile(
                userId = userPreferences.getUserId()!!,
                accessToken = userPreferences.getAccessToken()!!
            ),
            networkService.fetchMyFeed(
                userId = userPreferences.getUserId()!!,
                accessToken = userPreferences.getAccessToken()!!
            ),
            BiFunction { myProfileResponse, myPostListResponse ->
                myProfileResponse.user.also {
                    it.postCount = myPostListResponse.data.count()
                }
            }
        )

    private fun mapUserResponseAndSaveIntoPref(response: UserResponse): User =
        User(response.userId, response.userName, response.userEmail, response.accessToken).also {
            saveCurrentUser(user = it)
        }
}