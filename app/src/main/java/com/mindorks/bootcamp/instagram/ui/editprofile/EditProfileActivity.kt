package com.mindorks.bootcamp.instagram.ui.editprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.data.model.Avatar
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.utils.display.Toaster
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import java.io.FileNotFoundException

class EditProfileActivity : BaseActivity<EditProfileViewModel>() {

    private lateinit var menu: Menu

    override fun provideLayoutId(): Int = R.layout.activity_edit_profile

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.getParcelable<Avatar>(EXTRA_CURRENT_USER)?.run {
            viewModel.setCurrentUser(this)
        }
    }

    override fun setupView(savedInstanceState: Bundle?) {
        toolbar.title = getString(R.string.editprofile_title)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_cancel)
        }

        tv_placeholder.setOnClickListener {
            Intent(Intent.ACTION_PICK)
                .apply {
                    type = "image/*"
                }.run {
                    startActivityForResult(this, RESULT_GALLERY)
                }
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.isAllFieldEmpty.observe(this, Observer {
            if (it) {
                Toaster.show(this, getString(R.string.editprofile_empty_field))
            }
        })

        viewModel.getCurrentUser().observe(this, Observer {
            it.run {
                et_name.setText(name)
                et_bio.setText(tagLine)
            }
        })

        val renderPhotoObserver = Observer<GlideUrl> {
            Glide.with(iv_placeholder)
                .load(it)
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE))
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.placeholderOf(R.drawable.ic_profile_add_pic))
                .into(iv_placeholder)
        }

        viewModel.getProfilePicture().observe(this, renderPhotoObserver)
        viewModel.getUploadedPhoto().observe(this, renderPhotoObserver)

        viewModel.email.observe(this, Observer {
            et_email.setText(it ?: "")
        })

        viewModel.isLoading.observe(this, Observer {
            menu.findItem(R.id.menu_confirm).apply {
                isEnabled = !it
            }
            progress_bar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.launchHomeActivity.observe(this, Observer {
            Toaster.show(this, getString(R.string.editprofile_success_text))
            it.getIfNotHandled()?.let {
                finish()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_GALLERY -> {
                    try {
                        intent?.data?.let {
                            contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(getString(R.string.editprofile_select_image))
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(getString(R.string.editprofile_select_image))
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation_edit_profile, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_confirm -> {
                viewModel.updateProfileInformation(
                    et_name.text.toString(),
                    et_bio.text.toString()
                )
                true
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_CURRENT_USER: String = ""
        const val RESULT_GALLERY: Int = 101

        fun getIntent(context: Context, currentUser: Avatar): Intent =
            Intent(context, EditProfileActivity::class.java).apply {
                putExtra(EXTRA_CURRENT_USER, currentUser)
            }
    }
}