package com.mindorks.bootcamp.instagram.ui.home

import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity

class HomeActivity : BaseActivity<HomeViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_home

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
    }

    private fun addFragment() {

    }
}