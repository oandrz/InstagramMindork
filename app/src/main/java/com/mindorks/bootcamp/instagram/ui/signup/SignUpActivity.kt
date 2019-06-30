package com.mindorks.bootcamp.instagram.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.home.HomeActivity
import com.mindorks.bootcamp.instagram.ui.login.LoginActivity
import com.mindorks.bootcamp.instagram.utils.common.Status
import com.mindorks.bootcamp.instagram.utils.component.addClearDrawable
import com.mindorks.bootcamp.instagram.utils.component.removeClearDrawable
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : BaseActivity<SignUpViewModel>() {
    override fun provideLayoutId(): Int = R.layout.activity_signup

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
    }

    override fun setupView(savedInstanceState: Bundle?) {
        et_email.addClearDrawable()
        et_name.addClearDrawable()

        btn_signup.setOnClickListener {
            viewModel.signUp(
                    et_name.text.toString(),
                    et_email.text.toString(),
                    et_password.text.toString()
            )
        }

        tv_login_navigation.setOnClickListener { viewModel.launchLogin() }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.isFetchingApi().observe(this, Observer {
            with(btn_signup) {
                if (it) {
                    isEnabled = false
                    setText(R.string.general_loading)
                } else {
                    isEnabled = true
                    setText(R.string.signup_button_signup_text)
                }
            }
        })

        viewModel.nameValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> container_name.error = it.data?.run { getString(this) }
                else -> container_name.isErrorEnabled = false
            }
        })

        viewModel.emailValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> container_email.error = it.data?.run { getString(this) }
                else -> container_email.isErrorEnabled = false
            }
        })

        viewModel.passwordValidation.observe(this, Observer {
            when (it.status) {
                Status.ERROR -> container_name.error = it.data?.run { getString(this) }
                else -> container_name.isErrorEnabled = false
            }
        })

        viewModel.launchLogin.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(LoginActivity.getIntent(this@SignUpActivity))
                finish()
            }
        })

        viewModel.launchHome.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(HomeActivity.getIntent(this@SignUpActivity))
                finish()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        et_name.removeClearDrawable()
        et_email.removeClearDrawable()
    }

    companion object {
        @JvmStatic
        fun getIntent(context: Context) = Intent(context, SignUpActivity::class.java)
    }
}