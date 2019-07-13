package com.mindorks.bootcamp.instagram.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.create.CreateFragment
import com.mindorks.bootcamp.instagram.ui.feed.FeedFragment
import com.mindorks.bootcamp.instagram.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.activity_home.*
import java.lang.IllegalStateException
import javax.inject.Inject

class HomeActivity : BaseActivity<HomeViewModel>() {

    @Inject
    lateinit var homeSharedViewModel: HomeSharedViewModel

    private var activeFragment: Fragment? = null

    override fun provideLayoutId(): Int = R.layout.activity_home

    override fun injectDependencies(activityComponent: ActivityComponent) =
        activityComponent.inject(this)

    override fun setupView(savedInstanceState: Bundle?) {
        navigation.run {
            itemIconTintList = null
            setOnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_feed -> {
                        viewModel.onHomeSelected()
                        true
                    }
                    R.id.menu_create -> {
                        viewModel.onCreateFeedSelected()
                        true
                    }
                    R.id.menu_profile -> {
                        viewModel.onProfileSelected()
                        true
                    }
                    else -> false
                }
            }
            selectedItemId = R.id.menu_feed
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.homeNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run {
                addFragment(FeedFragment.TAG)
            }
        })

        viewModel.createNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run {
                addFragment(CreateFragment.TAG)
            }
        })

        viewModel.profileNavigation.observe(this, Observer {
            it.getIfNotHandled()?.run {
                addFragment(ProfileFragment.TAG)
            }
        })

        homeSharedViewModel.homeRedirection.observe(this, Observer {
            it.getIfNotHandled()?.run { navigation.selectedItemId = R.id.menu_feed }
        })
    }

    private fun addFragment(tag: String) {
        when (activeFragment) {
            is FeedFragment -> if (tag == FeedFragment.TAG) return
            is CreateFragment -> if (tag == CreateFragment.TAG) return
            is ProfileFragment -> if (tag == ProfileFragment.TAG) return
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment == null) {
            fragment = when (tag) {
                FeedFragment.TAG -> FeedFragment.newInstance()
                CreateFragment.TAG -> CreateFragment.newInstance()
                ProfileFragment.TAG -> ProfileFragment.newInstance()
                else -> throw IllegalStateException("unknown navigation type")
            }
            fragmentTransaction.add(R.id.navigation_fragment_container, fragment, tag)
        } else {
            fragmentTransaction.show(fragment)
        }

        if (activeFragment != null) fragmentTransaction.hide(activeFragment as Fragment)

        fragmentTransaction.commit()

        activeFragment = fragment
    }

    companion object {

        @JvmStatic
        fun getIntent(context: Context): Intent = Intent(context, HomeActivity::class.java)
    }
}