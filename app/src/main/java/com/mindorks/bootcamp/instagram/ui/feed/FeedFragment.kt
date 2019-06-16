package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment

class FeedFragment : BaseFragment<FeedViewModel>() {
    override fun provideLayoutId(): Int = 0

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
    }

    override fun setupView(view: View) {
    }
}