package com.mindorks.bootcamp.instagram.ui.feed

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Feed
import com.mindorks.bootcamp.instagram.di.component.ViewHolderComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseItemViewHolder
import com.mindorks.bootcamp.instagram.utils.common.GlideHelper
import com.mindorks.bootcamp.instagram.utils.common.TimeUtils
import com.mindorks.bootcamp.instagram.utils.common.getDateWithServerTimeStamp
import kotlinx.android.synthetic.main.item_feed.view.*
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
        viewModel.userName.observe(this, Observer {
            itemView.tv_name.text = it
        })

        viewModel.profilePicture.observe(this, Observer {
            it?.run {
                Glide.with(itemView.iv_avatar.context)
                    .load(GlideHelper.getProtectedUrl(it.imageUrl, glideHeader))
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                    .apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_signup))
                    .into(itemView.iv_avatar)
            }
        })

        viewModel.image.observe(this, Observer {
            it?.run {
                val glideRequest = Glide.with(itemView.context)
                    .load(GlideHelper.getProtectedUrl(it.imageUrl, glideHeader))
                    .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))

                if (imageWidth > 0 && imageHeight > 0) {
                    val params = itemView.iv_image.layoutParams as ViewGroup.LayoutParams
                    params.width = imageWidth
                    params.height = imageHeight
                    itemView.iv_image.layoutParams = params
                    glideRequest.apply(RequestOptions.overrideOf(imageWidth, imageHeight))
                }

                glideRequest.into(itemView.iv_image)
            }
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

        viewModel.hasLiked.observe(this, Observer {
            itemView.iv_favourite.setImageResource(
                if (it) R.drawable.ic_heart_selected
                else R.drawable.ic_heart_unselected
            )

        })

        viewModel.showFavouriteAnimation.observe(this, Observer {
            itemView.favourite_animation.run {
                visibility = View.VISIBLE
                speed = if (it == ShowMode.REVERSE) -1f else 1f
                favourite_animation.playAnimation()
            }
        })
    }

    override fun setupView(view: View) {
        view.setOnLongClickListener {
            viewModel.onFavouriteIconClicked()
            it.isLongClickable
        }

        view.iv_favourite.setOnClickListener {
            viewModel.onFavouriteIconClicked()
        }

        itemView.favourite_animation.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                itemView.favourite_animation.run {
                    clearAnimation()
                    visibility = View.GONE
                }
            }
        })
    }

    override fun onDestroy() {
        itemView.favourite_animation.run {
            clearAnimation()
            removeAllAnimatorListeners()
        }
        super.onDestroy()
    }
}