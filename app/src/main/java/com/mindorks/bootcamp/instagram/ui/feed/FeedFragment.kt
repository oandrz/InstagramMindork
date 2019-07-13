package com.mindorks.bootcamp.instagram.ui.feed

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.home.HomeSharedViewModel
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import javax.inject.Inject

class FeedFragment : BaseFragment<FeedViewModel>() {

    @Inject
    lateinit var homeSharedViewModel: HomeSharedViewModel

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
            }
        })

        viewModel.isRefresh.observe(this, Observer {
            if (it) {
                feedAdapter.clearAllData()
            }
        })

        viewModel.loading.observe(this, Observer {
            loadmore_progress.visibility = if (it) View.VISIBLE else View.GONE
        })

        homeSharedViewModel.newPost.observe(this, Observer {
            it.getIfNotHandled()?.run { viewModel.onNewPost(this) }
        })

        viewModel.refreshFeed.observe(this, Observer {
            it.data?.run {
                feedAdapter.updateList(this)
                rv_feed.scrollToPosition(0)
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
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    layoutManager?.run {
                        if (this is LinearLayoutManager
                            && itemCount > 0
                            && itemCount == findLastVisibleItemPosition() + 1
                        ) {
                            viewModel.onLoadMore()
                        }
                    }
                }
            })
        }

        container_refresh.isRefreshing = false
        container_refresh.setOnRefreshListener {
            container_refresh.isRefreshing = false
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