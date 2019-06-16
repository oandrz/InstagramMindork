package com.mindorks.bootcamp.instagram.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.utils.common.*
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class LoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val userRepository: UserRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    private val validationList: MutableLiveData<List<Validation>> = MutableLiveData()
    private val userLiveData: MutableLiveData<Resource<User>> = MutableLiveData()

    val launchHome: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()
    val launchSignUp: MutableLiveData<Event<Map<String, String>>> = MutableLiveData()

    val emailValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.EMAIL)
    val passwordValidation: LiveData<Resource<Int>> = filterValidation(Validation.Field.PASSWORD)

    fun isFetchingApi(): LiveData<Boolean> =
        Transformations.map(userLiveData) { it.status == Status.LOADING }

    override fun onCreate() {}

    fun login(email: String, password: String) {
        val validations = Validator.validateLoginFields(email, password)
        validationList.postValue(validations)

        if (validations.isNotEmpty()) {
            val successValidation = validations.filter { it.resource.status == Status.SUCCESS }

            if (successValidation.size == validations.size && checkInternetConnectionWithMessage()) {
                userLiveData.postValue(Resource.loading())
                compositeDisposable.add(
                        userRepository
                                .doLogin(email, password)
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
                                )
                )
            }
        }
    }

    fun launchSignUp() = launchSignUp.postValue(Event(emptyMap()))

    private fun filterValidation(field: Validation.Field) =
            Transformations.map(validationList) {
                it.find { validation -> validation.field == field }
                        ?.run { return@run this.resource }
                        ?: Resource.unknown()
            }
}