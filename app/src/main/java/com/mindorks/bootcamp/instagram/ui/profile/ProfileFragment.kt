package com.mindorks.bootcamp.instagram.ui.profile

import android.view.View
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment

class ProfileFragment : BaseFragment<ProfileViewModel>() {
    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
    }

    companion object {
        const val TAG = "profile"

        @JvmStatic
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}