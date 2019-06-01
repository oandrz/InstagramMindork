package com.mindorks.bootcamp.instagram.ui.login

import android.os.Bundle
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity

class LoginActivity : BaseActivity<LoginViewModel>() {
    override fun provideLayoutId(): Int {
        return 0
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }
}