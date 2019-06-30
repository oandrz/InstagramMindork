package com.mindorks.bootcamp.instagram.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
import com.mindorks.bootcamp.instagram.ui.home.HomeActivity
import com.mindorks.bootcamp.instagram.ui.signup.SignUpActivity
import com.mindorks.bootcamp.instagram.utils.common.Status
import com.mindorks.bootcamp.instagram.utils.component.addClearDrawable
import com.mindorks.bootcamp.instagram.utils.component.removeClearDrawable
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
            viewModel.launchSignUp()
        }
    }

    override fun setupObservers() {
        super.setupObservers()
        viewModel.isFetchingApi().observe(this, Observer {
            with(btn_login) {
                if (it) {
                    isEnabled = false
                    text = getString(R.string.general_loading)
                } else {
                    isEnabled = true
                    text = getString(R.string.login_button_text)
                }
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

        viewModel.launchSignUp.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(SignUpActivity.getIntent(this@LoginActivity))
                finish()
            }
        })

        viewModel.launchHome.observe(this, Observer {
            it.getIfNotHandled()?.run {
                startActivity(HomeActivity.getIntent(this@LoginActivity))
                finish()
            }
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