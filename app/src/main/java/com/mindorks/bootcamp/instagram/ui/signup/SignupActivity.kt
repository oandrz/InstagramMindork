package com.mindorks.bootcamp.instagram.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity

class SignupActivity : BaseActivity<SignupViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_signup

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

    override fun setupObservers() {
        super.setupObservers()
    }

    companion object {

        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }
}