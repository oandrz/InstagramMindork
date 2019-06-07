package com.mindorks.bootcamp.instagram.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.signup.SignupActivity
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
        et_password.addClearDrawable()

        tv_signup_navigation.setOnClickListener {
            startActivity(SignupActivity.getIntent(this))
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getUser().observe(this, Observer {
            it?.let {
                Toaster.show(this, "success")
            }
        })
        viewModel.isAuthenticating().observe(this, Observer {
            if (it) {
                freezeUI(true)
                Toaster.show(this, "loading")
            } else {
                freezeUI(false)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        et_email.removeClearDrawable()
        et_password.removeClearDrawable()
    }

    private fun freezeUI(isEnabled: Boolean) {
        et_email.isEnabled = !isEnabled
        et_password.isEnabled = !isEnabled
        btn_login.isEnabled = !isEnabled
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)
    }
}