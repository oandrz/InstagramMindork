package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.di.component.ViewHolderComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewHolder
import com.mindorks.bootcamp.instagram.utils.common.GlideHelper
import com.mindorks.bootcamp.instagram.utils.common.TimeUtils
import com.mindorks.bootcamp.instagram.utils.common.getDateWithServerTimeStamp
import kotlinx.android.synthetic.main.item_feed.view.*
import java.text.SimpleDateFormat
import javax.inject.Inject

class FeedItemViewHolder(parent: ViewGroup) :
        BaseItemViewHolder<Feed, FeedItemViewModel>(R.layout.item_feed, parent) {

    @Inject
    lateinit var glideHeader: Map<String, String>

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.user.observe(this, Observer {
            with(itemView) {
                tv_name.text = it.name
                if (!it.profilePictureUrl.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(GlideHelper.getProtectedUrl(it.profilePictureUrl, glideHeader))
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                        .apply(RequestOptions.circleCropTransform())
                        .error(Glide.with(context).load(R.drawable.ic_signup))
                        .into(itemView.iv_avatar)
                }
            }
        })

        viewModel.image.observe(this, Observer {
            Glide.with(itemView.context)
                .load(GlideHelper.getProtectedUrl(it.imageUrl, glideHeader))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .into(itemView.iv_image)
        })

        viewModel.likesCount.observe(this, Observer {
            itemView.tv_likes_count.text = String.format(
                itemView.context.resources
                    .getString(R.string.feed_likes_format), it
            )
        })

        viewModel.timeStamp.observe(this, Observer {
            itemView.tv_time.text = TimeUtils.getTimeAgo(it.getDateWithServerTimeStamp())
        })

    }

    override fun setupView(view: View) {
        view.setOnClickListener {

        }
    }
}