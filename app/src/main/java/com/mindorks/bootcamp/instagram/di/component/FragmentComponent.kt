package com.mindorks.bootcamp.instagram.di.component

import com.mindorks.bootcamp.instagram.di.FragmentScope
import com.mindorks.bootcamp.instagram.di.module.FragmentModule
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.base.BaseViewModel
import com.mindorks.bootcamp.instagram.ui.create.CreateFragment
import com.mindorks.bootcamp.instagram.ui.dummies.DummiesFragment
import com.mindorks.bootcamp.instagram.ui.feed.FeedFragment
import com.mindorks.bootcamp.instagram.ui.profile.ProfileFragment
import dagger.Component

@FragmentScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [FragmentModule::class]
)
interface FragmentComponent {

    fun inject(fragment: DummiesFragment)

    fun inject(fragment: FeedFragment)

    fun inject(fragment: CreateFragment)

    fun inject(fragment: ProfileFragment)
}