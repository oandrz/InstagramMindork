package com.mindorks.bootcamp.instagram.ui.login

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
class LoginViewModelUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkHelper: NetworkHelper

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var launchHomeObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var launchSignupObserver: Observer<Event<Map<String, String>>>

    @Mock
    private lateinit var messageStringResId: Observer<Resource<Int>>

    private lateinit var testScheduler: TestScheduler

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        val compositeDisposable = CompositeDisposable()
        testScheduler = TestScheduler()
        val testSchedulerProvider = TestSchedulerProvider(testScheduler)
        loginViewModel = LoginViewModel(
            testSchedulerProvider,
            compositeDisposable,
            networkHelper,
            userRepository
        )
        loginViewModel.run {
            launchHome.observeForever(launchHomeObserver)
            launchSignUp.observeForever(launchSignupObserver)
            messageStringId.observeForever(messageStringResId)
        }
    }

    @Test
    fun givenResponse200_whenLogin_shouldLaunchHomeActivity() {
        val email = "test@gmail.com"
        val password = "password"
        val user = User("id", "test", email, "accessToken")
        doReturn(true)
            .`when`(networkHelper)
            .isNetworkConnected()
        doReturn(Single.just(user))
            .`when`(userRepository)
            .doLogin(email, password)
        loginViewModel.login(email, password)
        testScheduler.triggerActions()
        assert(loginViewModel.launchHome.value == Event(hashMapOf<String, String>()))
        Mockito.verify(launchHomeObserver).onChanged(Event(hashMapOf()))
    }

    @Test
    fun givenNoInternet_whenLogin_shouldShowNetworkError() {
        val email = "test@gmail.com"
        val password = "password"
        doReturn(false)
            .`when`(networkHelper)
            .isNetworkConnected()
        loginViewModel.login(email, password)
        assert(loginViewModel.messageStringId.value == Resource.error(R.string.network_connection_error))
        Mockito.verify(messageStringResId).onChanged(Resource.error(R.string.network_connection_error))
    }

    @Test
    fun givenOnclick_whenSignupNavigationEnabled_shouldLaunchSignupScreen() {
        loginViewModel.launchSignUp()
        assert(loginViewModel.launchSignUp.value == Event(hashMapOf<String, String>()))
        Mockito.verify(launchSignupObserver).onChanged(Event(hashMapOf()))
    }
}