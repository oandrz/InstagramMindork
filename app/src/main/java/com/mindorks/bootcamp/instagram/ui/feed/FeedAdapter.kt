package com.mindorks.bootcamp.instagram.ui.feed

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.ui.base.BaseAdapter

class FeedAdapter(
        parentLifecycle: Lifecycle,
        private val feeds: ArrayList<Feed>
) : BaseAdapter<Feed, FeedItemViewHolder>(parentLifecycle, feeds) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder =
            FeedItemViewHolder(parent)
}