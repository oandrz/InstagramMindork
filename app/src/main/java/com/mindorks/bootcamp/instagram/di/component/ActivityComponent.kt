package com.mindorks.bootcamp.instagram.di.component

import com.mindorks.bootcamp.instagram.di.ActivityScope
import com.mindorks.bootcamp.instagram.di.module.ActivityModule
import com.mindorks.bootcamp.instagram.ui.dummy.DummyActivity
import com.mindorks.bootcamp.instagram.ui.editprofile.EditProfileActivity
import com.mindorks.bootcamp.instagram.ui.home.HomeActivity
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import com.mindorks.bootcamp.instagram.ui.signup.SignUpActivity
import com.mindorks.bootcamp.instagram.ui.splash.SplashActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ActivityModule::class]
)
interface ActivityComponent {

    fun inject(activity: SplashActivity)

    fun inject(activity: DummyActivity)

    fun inject(activity: LoginActivity)

    fun inject(activity: SignUpActivity)

    fun inject(activity: HomeActivity)

    fun inject(activity: EditProfileActivity)
}