package com.mindorks.bootcamp.instagram.di.module

import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.bootcamp.instagram.data.repository.DummyRepository
import com.mindorks.bootcamp.instagram.data.repository.FeedRepository
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.create.CreateViewModel
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesAdapter
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesViewModel
import com.mindorks.bootcamp.instagram.ui.feed.FeedAdapter
import com.mindorks.bootcamp.instagram.ui.feed.FeedViewModel
import com.mindorks.bootcamp.instagram.ui.profile.ProfileViewModel
import com.mindorks.bootcamp.instagram.utils.ViewModelProviderFactory
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.SchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class FragmentModule(private val fragment: BaseFragment<*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideDummiesViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            dummyRepository: DummyRepository
    ): DummiesViewModel =
            ViewModelProviders.of(fragment, ViewModelProviderFactory(DummiesViewModel::class) {
                DummiesViewModel(schedulerProvider, compositeDisposable, networkHelper,
                        dummyRepository)
            }).get(DummiesViewModel::class.java)

    @Provides
    fun provideFeedViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper,
            feedRepository: FeedRepository
    ): FeedViewModel =
            ViewModelProviders.of(fragment, ViewModelProviderFactory(FeedViewModel::class) {
                FeedViewModel(schedulerProvider, compositeDisposable, networkHelper, feedRepository)
            }).get(FeedViewModel::class.java)

    @Provides
    fun provideCreateViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper
    ): CreateViewModel =
            ViewModelProviders.of(fragment, ViewModelProviderFactory(CreateViewModel::class) {
                CreateViewModel(schedulerProvider, compositeDisposable, networkHelper)
            }).get(CreateViewModel::class.java)

    @Provides
    fun provideProfileViewModel(
            schedulerProvider: SchedulerProvider,
            compositeDisposable: CompositeDisposable,
            networkHelper: NetworkHelper
    ): ProfileViewModel =
            ViewModelProviders.of(fragment, ViewModelProviderFactory(ProfileViewModel::class) {
                ProfileViewModel(schedulerProvider, compositeDisposable, networkHelper)
            }).get(ProfileViewModel::class.java)

    @Provides
    fun provideDummiesAdapter() = DummiesAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideFeedAdapter() = FeedAdapter(fragment.lifecycle, ArrayList())

    @Provides
    fun provideDividerItemDecoration(linearLayoutManager: LinearLayoutManager) =
            DividerItemDecoration(fragment.context, linearLayoutManager.orientation)
}