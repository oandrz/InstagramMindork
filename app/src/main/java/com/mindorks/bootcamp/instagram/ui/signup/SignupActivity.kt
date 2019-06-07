package com.mindorks.bootcamp.instagram.ui.signup

import android.os.Bundle
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity

class SignupActivity : BaseActivity<SignupViewModel>() {
    override fun provideLayoutId(): Int = 0


    override fun injectDependencies(activityComponent: ActivityComponent) {
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }
}