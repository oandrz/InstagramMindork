package com.mindorks.bootcamp.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.DummyRepository
import com.mindorks.bootcamp.instagram.data.repository.PhotoRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.di.TempDirectory
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.dummy.DummyViewModel
import com.mindorks.bootcamp.instagram.ui.editprofile.EditProfileViewModel
import com.mindorks.bootcamp.instagram.ui.home.HomeSharedViewModel
import com.mindorks.bootcamp.instagram.ui.home.HomeViewModel
import com.mindorks.bootcamp.instagram.ui.login.LoginViewModel
import com.mindorks.bootcamp.instagram.ui.signup.SignUpViewModel
import com.mindorks.bootcamp.instagram.ui.splash.SplashViewModel
import com.mindorks.bootcamp.instagram.utils.ViewModelProviderFactory
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import java.io.File

/**
 * Kotlin Generics Reference: https://kotlinlang.org/docs/reference/generics.html
 * Basically it means that we can pass any class that extends BaseActivity which take
 * BaseViewModel subclass as parameter
 */
@Module
class ActivityModule(private val activity: BaseActivity<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(activity)

    @Provides
    fun provideSplashViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): SplashViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(SplashViewModel::class) {
            SplashViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
            //this lambda creates and return SplashViewModel
        }).get(SplashViewModel::class.java)

    @Provides
    fun provideDummyViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        dummyRepository: DummyRepository
    ): DummyViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(DummyViewModel::class) {
            DummyViewModel(schedulerProvider, compositeDisposable, networkHelper, dummyRepository)
        }).get(DummyViewModel::class.java)

    @Provides
    fun provideLoginViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): LoginViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(LoginViewModel::class) {
            LoginViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(LoginViewModel::class.java)

    @Provides
    fun provideSignUpViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            userRepository: UserRepository
    ): SignUpViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(SignUpViewModel::class) {
            SignUpViewModel(schedulerProvider, compositeDisposable, networkHelper, userRepository)
        }).get(SignUpViewModel::class.java)

    @Provides
    fun provideHomeViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper
    ): HomeViewModel = ViewModelProviders.of(
            activity, ViewModelProviderFactory(HomeViewModel::class) {
        HomeViewModel(schedulerProvider, compositeDisposable, networkHelper)
    }).get(HomeViewModel::class.java)


    @Provides
    fun provideEditProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        @TempDirectory directory: File
    ): EditProfileViewModel =
        ViewModelProviders.of(activity, ViewModelProviderFactory(EditProfileViewModel::class) {
            EditProfileViewModel(schedulerProvider, compositeDisposable,
                networkHelper, userRepository, photoRepository, directory
            )
        }).get(EditProfileViewModel::class.java)

    @Provides
    fun provideHomeSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): HomeSharedViewModel = ViewModelProviders.of(
        activity, ViewModelProviderFactory(HomeSharedViewModel::class) {
            HomeSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(HomeSharedViewModel::class.java)
}