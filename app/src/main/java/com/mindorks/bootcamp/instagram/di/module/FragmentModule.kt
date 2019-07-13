package com.mindorks.bootcamp.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.bootcamp.instagram.data.repository.DummyRepository
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.data.repository.PhotoRepository
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.di.TempDirectory
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.create.CreateViewModel
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesAdapter
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesViewModel
import com.mindorks.bootcamp.instagram.ui.editprofile.EditProfileViewModel
import com.mindorks.bootcamp.instagram.ui.feed.FeedAdapter
import com.mindorks.bootcamp.instagram.ui.feed.FeedViewModel
import com.mindorks.bootcamp.instagram.ui.home.HomeSharedViewModel
import com.mindorks.bootcamp.instagram.ui.profile.ProfileViewModel
import com.mindorks.bootcamp.instagram.utils.ViewModelProviderFactory
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import com.mindorks.paracamera.Camera
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import java.io.File

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideCamera(): Camera = Camera.Builder()
        .resetToCorrectOrientation(true)
        .setTakePhotoRequestCode(1)
        .setDirectory("temp")
        .setName("camera_temp_img")
        .setImageFormat(Camera.IMAGE_JPEG)
        .setCompression(75)
        .setImageHeight(500)
        .build(fragment)

    @Provides
    fun provideDummiesViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        dummyRepository: DummyRepository
    ): DummiesViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(DummiesViewModel::class) {
            DummiesViewModel(
                schedulerProvider, compositeDisposable, networkHelper,
                dummyRepository
            )
        }).get(DummiesViewModel::class.java)

    @Provides
    fun provideFeedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        feedRepository: FeedRepository
    ): FeedViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(FeedViewModel::class) {
            FeedViewModel(
                schedulerProvider, compositeDisposable, networkHelper,
                feedRepository, ArrayList(), PublishProcessor.create()
            )
        }).get(FeedViewModel::class.java)

    @Provides
    fun provideCreateViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository,
        photoRepository: PhotoRepository,
        feedRepository: FeedRepository,
        @TempDirectory directory: File
    ): CreateViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(CreateViewModel::class) {
            CreateViewModel(
                schedulerProvider, compositeDisposable, networkHelper,
                userRepository, photoRepository, feedRepository, directory
            )
        }).get(CreateViewModel::class.java)

    @Provides
    fun provideProfileViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        userRepository: UserRepository
    ): ProfileViewModel =
        ViewModelProviders.of(fragment, ViewModelProviderFactory(ProfileViewModel::class) {
            ProfileViewModel(
                schedulerProvider, compositeDisposable,
                networkHelper, userRepository
            )
        }).get(ProfileViewModel::class.java)

    @Provides
    fun provideDummiesAdapter() = DummiesAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideFeedAdapter() = FeedAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideDividerItemDecoration(linearLayoutManager: LinearLayoutManager) =
        DividerItemDecoration(fragment.context, linearLayoutManager.orientation)

    @Provides
    fun provideHomeSharedViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper
    ): HomeSharedViewModel = ViewModelProviders.of(
        fragment.activity!!, ViewModelProviderFactory(HomeSharedViewModel::class) {
            HomeSharedViewModel(schedulerProvider, compositeDisposable, networkHelper)
        }).get(HomeSharedViewModel::class.java)
}