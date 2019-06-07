package com.mindorks.bootcamp.instagram.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.utils.component.addClearDrawable
import com.mindorks.bootcamp.instagram.utils.component.removeClearDrawable
import com.mindorks.bootcamp.instagram.utils.display.Toaster
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {

    override fun provideLayoutId(): Int = R.layout.activity_login

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        btn_login.setOnClickListener {
            viewModel.login(et_email.text.toString(), et_password.text.toString())
        }

        et_email.addClearDrawable()

        tv_signup_navigation.setOnClickListener {
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getUser().observe(this, Observer {
            Toaster.show(this, "success")
        })
    }

    override fun onStop() {
        super.onStop()
        et_email.removeClearDrawable()
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }
}