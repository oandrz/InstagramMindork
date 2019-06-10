package com.mindorks.bootcamp.instagram.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import com.mindorks.bootcamp.instagram.utils.component.addClearDrawable
import com.mindorks.bootcamp.instagram.utils.component.removeClearDrawable
import com.mindorks.bootcamp.instagram.utils.display.Toaster
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : BaseActivity<SignUpViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_signup

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        et_email.addClearDrawable()
        et_password.addClearDrawable()
        et_name.addClearDrawable()
        btn_signup.setOnClickListener {
            viewModel.signUp(et_name.text.toString(), et_email.text.toString(), et_password.text.toString())
        }
        tv_login_navigation.setOnClickListener {
            viewModel.launchLogin()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.getUser().observe(this, Observer {
            it?.let {
                Toaster.show(this, "success")
            }
        })

        viewModel.isFetchingApi().observe(this, Observer {
            if (it) {
                freezeUI(true)
                Toaster.show(this, "loading")
            } else {
                freezeUI(false)
            }
        })

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(LoginActivity.getIntent(this@SignUpActivity))
                finish()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        et_name.removeClearDrawable()
        et_email.removeClearDrawable()
        et_password.removeClearDrawable()
    }

    private fun freezeUI(isEnabled: Boolean) {
        et_email.isEnabled = !isEnabled
        et_password.isEnabled = !isEnabled
        et_name.isEnabled = !isEnabled
        btn_signup.isEnabled = !isEnabled
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignUpActivity::class.java)
    }
}