package com.mindorks.bootcamp.instagram.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.TestSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HomeViewModelUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var launchHomeFragmentObserver: Observer<Event<Boolean>>

    @Mock
    private lateinit var launchProfileFragmentObserver: Observer<Event<Boolean>>

    @Mock
    private lateinit var launchCreateFeedFragmentObserver: Observer<Event<Boolean>>

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var testScheduler: TestScheduler

    @Before
    fun setup() {
        testScheduler = TestScheduler()
        val testSchedulerprovider = TestSchedulerProvider(testScheduler)
        homeViewModel = HomeViewModel(
            testSchedulerprovider,
            CompositeDisposable(),
            networkHelper
        ).also {
            it.homeNavigation.observeForever(launchHomeFragmentObserver)
            it.createNavigation.observeForever(launchCreateFeedFragmentObserver)
            it.profileNavigation.observeForever(launchProfileFragmentObserver)
        }
    }

    @Test
    fun givenOnProfileFragment_whenNavigationPositionTwoClicked_shouldNavigateToCreateFeedFragment() {
        homeViewModel.onCreateFeedSelected()
        assert(homeViewModel.createNavigation.value == Event(true))
        verify(launchCreateFeedFragmentObserver).onChanged(Event(true))
    }

    @Test
    fun givenOnFeedFragment_whenNavigationPositionThreeClicked_shouldNavigateToProfileFragment() {
        homeViewModel.onProfileSelected()
        assert(homeViewModel.profileNavigation.value == Event(true))
        verify(launchProfileFragmentObserver).onChanged(Event(true))
    }

    @Test
    fun givenOnProfileFragment_whenNavigationPositionOneClicked_shouldNavigateToFeedFragment() {
        homeViewModel.onHomeSelected()
        assert(homeViewModel.homeNavigation.value == Event(true))
        verify(launchHomeFragmentObserver).onChanged(Event(true))
    }
}