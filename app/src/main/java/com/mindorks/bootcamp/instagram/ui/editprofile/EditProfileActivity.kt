package com.mindorks.bootcamp.instagram.ui.editprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity

class EditProfileActivity : BaseActivity<EditProfileViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_edit_profile

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, EditProfileActivity::class.java)
    }
}