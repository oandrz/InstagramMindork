package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment

class FeedFragment : BaseFragment<FeedViewModel>() {
    override fun provideLayoutId(): Int = R.layout.fragment_feed

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
    }

    companion object {
        const val TAG: String = "Feed"

        fun newInstance() = FeedFragment()
    }
}