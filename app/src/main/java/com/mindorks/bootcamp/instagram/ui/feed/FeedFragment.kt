package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*

class FeedFragment : BaseFragment<FeedViewModel>() {
    override fun provideLayoutId(): Int = R.layout.fragment_feed

    override fun injectDependencies(fragmentComponent: FragmentComponent) = fragmentComponent.inject(this)


    override fun setupView(view: View) {
        iv_toolbar_logo.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            supportActionBar?.title = ""
        }
    }

    companion object {
        const val TAG: String = "Feed"

        fun newInstance() = FeedFragment()
    }
}