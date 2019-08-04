package com.mindorks.bootcamp.instagram.ui.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.User
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.bootcamp.instagram.utils.common.Resource
import com.mindorks.bootcamp.instagram.utils.network.NetworkHelper
import com.mindorks.bootcamp.instagram.utils.rx.TestSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.TestScheduler
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SignupViewModelUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var launchLoginObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var launchHomeObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var messageStringResId: Observer<Resource<Int>>

    private lateinit var testScheduler: TestScheduler

    private lateinit var signupViewModel: SignUpViewModel

    @Before
    fun setUp() {
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        signupViewModel = SignUpViewModel(
            testSchedulerProvider,
            compositeDisposable,
            networkHelper,
            userRepository
        )
        signupViewModel.run {
            launchHome.observeForever(launchHomeObserver)
            launchLogin.observeForever(launchLoginObserver)
            messageStringId.observeForever(messageStringResId)
        }
    }

    @Test
    fun givenResponse200_whenSignup_shouldLaunchHomeActivity() {
        val email = "test@gmail.com"
        val password = "password"
        val name = "testing name"
        val user = User(
            "id",
            "testing name",
            "test@gmail.com",
            "1234"
        )
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
        doReturn(Single.just(user))
            .`when`(userRepository)
            .doSignUp(email, password, name)
        signupViewModel.signUp(name, email, password)
        testScheduler.triggerActions()
        assert(signupViewModel.launchHome.value == Event(hashMapOf<String, String>()))
        Mockito.verify(launchHomeObserver).onChanged(Event(hashMapOf()))
    }

    @Test
    fun givenNoInternet_whenSignUp_shouldShowNetworkError() {
        val email = "test@gmail.com"
        val password = "password"
        val name = "testing name"
        val user = User(
            "id",
            "testing name",
            "test@gmail.com",
            "1234"
        )
        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()
        signupViewModel.signUp(name, email, password)
        testScheduler.triggerActions()
        assert(signupViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        Mockito.verify(messageStringResId).onChanged(Resource.error(R.string.network_connection_error))
    }

    @Test
    fun givenOnclick_whenLoginNavigationEnabled_shouldLaunchLoginScreen() {
        signupViewModel.launchLogin()
        assert(signupViewModel.launchLogin.value == Event(hashMapOf<String, String>()))
        Mockito.verify(launchLoginObserver).onChanged(Event(hashMapOf()))
    }
}