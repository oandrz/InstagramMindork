package com.mindorks.bootcamp.instagram.ui.profile

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.item_feed.view.*
import kotlinx.android.synthetic.main.toolbar.*

class ProfileFragment : BaseFragment<ProfileViewModel>() {
    override fun provideLayoutId(): Int = R.layout.fragment_profile

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupView(view: View) {
        setHasOptionsMenu(true)

        toolbar.title = getString(R.string.profile_title)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)

        btn_edit_profile.setOnClickListener {

        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getMyProfile().observe(this, Observer {
            tv_name.text = it.name
            tv_description.text = it.tagLine
            tv_post_count.text = getString(R.string.profile_post_count_format, it.postCount)
        })

        viewModel.getProfilePicture().observe(this, Observer {
            Glide.with(iv_avatar)
                .load(it)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_signup))
                .into(iv_avatar)
        })

        viewModel.isLoading.observe(this, Observer {
            group.visibility = if (it) View.GONE else View.VISIBLE
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                activity?.let {
                    startActivity(LoginActivity.getIntent(it))
                    it.finish()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_logout -> {
                viewModel.logout()
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val TAG = "profile"

        @JvmStatic
        fun newInstance(): ProfileFragment = ProfileFragment()
    }
}