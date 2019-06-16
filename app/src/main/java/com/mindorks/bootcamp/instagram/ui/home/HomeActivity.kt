package com.mindorks.bootcamp.instagram.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.ui.feed.FeedFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<HomeViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_home

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        with(navigation) {
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_feed -> {
                        addFragment(FeedFragment.TAG, FeedFragment.newInstance())
                        true
                    }
                    R.id.menu_create -> {
                        true
                    }
                    R.id.menu_profile -> {
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.menu_feed
        }


    }

    private fun addFragment(tag: String, fragment: BaseFragment<out BaseViewModel>) {
        supportFragmentManager.findFragmentByTag(tag) ?: supportFragmentManager
                .beginTransaction()
                .add(R.id.navigation_fragment_container, fragment, tag)
                .commitAllowingStateLoss()
    }

    companion object {

        @JvmStatic
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }

}