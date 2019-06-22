package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.data.remote.Networking
import com.mindorks.bootcamp.instagram.data.repository.UserRepository
import com.mindorks.bootcamp.instagram.di.component.ViewHolderComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewHolder
import com.mindorks.bootcamp.instagram.utils.common.GlideHelper
import kotlinx.android.synthetic.main.item_feed.view.*
import javax.inject.Inject

class FeedItemViewHolder(parent: ViewGroup) :
        BaseItemViewHolder<Feed, FeedItemViewModel>(R.layout.item_feed, parent) {

    @Inject
    lateinit var userRepository: UserRepository

    override fun injectDependencies(viewHolderComponent: ViewHolderComponent) {
        viewHolderComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.user.observe(this, Observer {
            with(itemView) {
                tv_name.text = it.name
                if (!it.profilePictureUrl.isNullOrEmpty()) {
                    Glide
                            .with(context)
                            .load(
//                                GlideHelper.getProtectedUrl(
//                                        it.profilePictureUrl,
//                                        mutableMapOf(
//                                                (Networking.HEADER_USER_ID to (userRepository.getCurrentUser()?.id ?: "")),
//                                                (Networking.HEADER_ACCESS_TOKEN to (userRepository.getCurrentUser()?.accessToken ?: "")),
//                                                (Networking.API_KEY to Networking.API_KEY)
//                                        )
//                                )
                                    it.profilePictureUrl
                            )
                            .into(itemView.iv_avatar)
                }
            }
        })

        viewModel.image.observe(this, Observer {
            Glide.with(itemView.context).load(it.imageUrl).into(itemView.iv_image)
        })

        viewModel.likesCount.observe(this, Observer {
            itemView.tv_likes_count.text = it.toString()
        })

        viewModel.timeStamp.observe(this, Observer {
            itemView.tv_time.text = it
        })

    }

    override fun setupView(view: View) {
        view.setOnClickListener {

        }
    }
}