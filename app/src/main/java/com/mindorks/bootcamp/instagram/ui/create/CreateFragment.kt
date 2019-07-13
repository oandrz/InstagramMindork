package com.mindorks.bootcamp.instagram.ui.create

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.FragmentComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseFragment
import com.mindorks.bootcamp.instagram.ui.home.HomeSharedViewModel
import com.mindorks.bootcamp.instagram.utils.common.Event
import com.mindorks.paracamera.Camera
import kotlinx.android.synthetic.main.fragment_create.*
import java.io.FileNotFoundException
import javax.inject.Inject

class CreateFragment : BaseFragment<CreateViewModel>() {

    @Inject
    lateinit var camera: Camera

    @Inject
    lateinit var homeSharedViewModel: HomeSharedViewModel

    override fun provideLayoutId(): Int = R.layout.fragment_create

    override fun injectDependencies(fragmentComponent: FragmentComponent) {
        fragmentComponent.inject(this)
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.loading.observe(this, Observer {
            pb_loading.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.post.observe(this, Observer {
            it.getIfNotHandled()?.let {
                homeSharedViewModel.apply {
                    newPost.postValue(Event(it))
                    onHomeRedirect()
                }
            }
        })
    }

    override fun setupView(view: View) {
        view_gallery.setOnClickListener {
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }.run {
                startActivityForResult(this, RESULT_GALLERY_IMG)
            }
        }

        view_camera.setOnClickListener {
            try {
                camera.takePicture()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RESULT_GALLERY_IMG -> {
                    try {
                        intent?.data?.let {
                            activity?.contentResolver?.openInputStream(it)?.run {
                                viewModel.onGalleryImageSelected(this)
                            }
                        } ?: showMessage(R.string.createPost_tryAgain_text)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                        showMessage(R.string.createPost_tryAgain_text)
                    }

                }
                Camera.REQUEST_TAKE_PHOTO -> {
                    viewModel.onCameraImageTaken { camera.cameraBitmapPath }
                }
            }
        }
    }

    companion object {
        const val TAG = "Create"
        const val RESULT_GALLERY_IMG = 1001

        @JvmStatic
        fun newInstance() = CreateFragment()
    }
}