package com.mindorks.bootcamp.instagram.di.component

import com.mindorks.bootcamp.instagram.di.ViewModelScope
import com.mindorks.bootcamp.instagram.di.module.ViewHolderModule
import com.mindorks.bootcamp.instagram.ui.dummies.DummyItemViewHolder
import com.mindorks.bootcamp.instagram.ui.feed.FeedItemViewHolder
import dagger.Component

@ViewModelScope
@Component(
    dependencies = [ApplicationComponent::class],
    modules = [ViewHolderModule::class]
)
interface ViewHolderComponent {
    fun inject(viewHolder: DummyItemViewHolder)
    fun inject(viewHolder: FeedItemViewHolder)
}