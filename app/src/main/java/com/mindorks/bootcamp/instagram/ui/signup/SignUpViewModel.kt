package com.mindorks.bootcamp.instagram.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class SignUpViewModel(
    schedulerProvider: SchedulerProvider,
    compositeDisposable: CompositeDisposable,
    networkHelper: NetworkHelper,
    private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val userLiveData: MutableLiveData<Resource<User>> = MutableLiveData()
    private val validationList: MutableLiveData<List<Validation>> = MutableLiveData()

    val nameValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.NAME)
    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    val launchLogin: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchHome: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    fun isFetchingApi(): LiveData<Boolean> =
        Transformations.map(userLiveData) { it.status == Status.LOADING }

    override fun onCreate() {

    }

    fun signUp(fullName: String, email: String, password: String) {
        val validations = Validator.validateSignUpFields(fullName, email, password)
        validationList.postValue(validations)

        if (validations.isNotEmpty()) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }
            if (successValidation.size == validations.size && checkInternetConnectionWithMessage()) {
                userLiveData.postValue(Resource.loading())
                compositeDisposable.add(
                    userRepository
                        .doSignUp(email, password, fullName)
                        .subscribeOn(schedulerProvider.io())
                        .subscribe(
                            {
                                userLiveData.postValue(Resource.success(it))
                                launchHome.postValue(Event(emptyMap()))
                            },
                            {
                                handleNetworkError(it)
                                userLiveData.postValue(Resource.error())
                            }
                        ))
            }
        }
    }

    fun launchLogin() = launchLogin.postValue(Event(emptyMap()))

    private fun filterValidation(field: Validation.Field) =
        Transformations.map(validationList) {
            it.find { validation -> validation.field == field }
                ?.run { return@run this.resource }
                ?: Resource.unknown()
        }
}