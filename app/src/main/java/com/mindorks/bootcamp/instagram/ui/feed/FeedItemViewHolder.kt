package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import android.view.ViewGroup
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.di.component.ViewHolderComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewHolder

class FeedItemViewHolder(parent: ViewGroup) :
        BaseItemViewHolder<Feed, FeedItemViewModel>(R.layout.item_feed, parent) {

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()

    }

    override fun setupView(view: View) {
        view.setOnClickListener {

        }
    }
}