package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class FeedFragment : BaseFragment<FeedViewModel>() {

    @Inject
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var feedAdapter: FeedAdapter

    @Inject
    lateinit var itemDecoration: DividerItemDecoration

    override fun provideLayoutId(): Int = R.layout.fragment_feed

    override fun injectDependencies(fragmentComponent: FragmentComponent) =
            fragmentComponent.inject(this)

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getFeeds().observe(this, Observer {
            it?.run {
                feedAdapter.appendData(this)
            }
        })

        viewModel.isFetchingFeed().observe(this, Observer {
            it?.run {
                rv_feed.visibility = if (this) View.GONE else View.VISIBLE
                progress_circle.visibility = if (this) View.VISIBLE else View.GONE
                container_refresh.isRefreshing = this
            }
        })
    }

    override fun setupView(view: View) {
        iv_toolbar_logo.visibility = View.VISIBLE
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            supportActionBar?.title = ""
        }

        rv_feed.run {
            addItemDecoration(itemDecoration)
            layoutManager = linearLayoutManager
            adapter = feedAdapter
        }

        container_refresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rv_feed.removeItemDecoration(itemDecoration)
    }

    companion object {
        const val TAG: String = "Feed"

        fun newInstance() = FeedFragment()
    }
}