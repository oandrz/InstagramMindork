package com.mindorks.bootcamp.instagram.ui.create

import android.view.View
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment

class CreateFragment : BaseFragment<CreateViewModel>() {
    override fun provideLayoutId(): Int = R.layout.fragment_create

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
    }

    companion object {
        const val TAG = "Create"

        @JvmStatic
        fun newInstance() = CreateFragment()
    }
}