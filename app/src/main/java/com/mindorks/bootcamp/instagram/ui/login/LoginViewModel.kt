package com.mindorks.bootcamp.instagram.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.common.Status
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDiposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDiposable, networkHelper) {

    private val userLiveData: MutableLiveData<Resource<User>> = MutableLiveData()

    val launchSignUp: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    fun getUser(): LiveData<User> =
        Transformations.map(userLiveData) { it.data }

    fun isFetchingApi(): LiveData<Boolean> =
        Transformations.map(userLiveData) { it.status == Status.LOADING }

    override fun onCreate() {

    }

    fun login(email: String, password: String) {
        ///TODO: Add Email Format Validation
        if (email.isEmpty() || password.isEmpty()) {
            messageStringId.postValue(
                Resource.error(
                    R.string.login_message_formempty_text
                )
            )
            return
        }
        if (checkInternetConnectionWithMessage()) {
            userLiveData.postValue(Resource.loading())
            ///TODO: Add hash algorithm for password
            compositeDisposable.add(
                userRepository
                    .doLogin(email, password)
                    .subscribeOn(schedulerProvider.io())
                    .subscribe(
                        {
                            userLiveData.postValue(Resource.success(it))
                        },
                        {
                            handleNetworkError(it)
                            userLiveData.postValue(Resource.error())
                        }
                    )
            )
        }
    }

    fun launchSignUp() = launchSignUp.postValue(Event(emptyMap()))
}