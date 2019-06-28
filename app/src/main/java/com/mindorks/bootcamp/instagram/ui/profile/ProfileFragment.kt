package com.mindorks.bootcamp.instagram.ui.profile

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*

class ProfileFragment : BaseFragment<ProfileViewModel>() {
    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        toolbar.title = getString(R.string.profile_title)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

    }

    override fun setupObservers() {
        super.setupObservers()
    }

    companion object {
        const val TAG = "profile"

        @JvmStatic
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}