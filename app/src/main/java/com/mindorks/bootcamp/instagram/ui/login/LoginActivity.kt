package com.mindorks.bootcamp.instagram.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.mindorks.bootcamp.instagram.R
import com.mindorks.bootcamp.instagram.di.component.ActivityComponent
import com.mindorks.bootcamp.instagram.ui.base.BaseActivity
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
            if (it) {
                btn_login.isEnabled = false
                btn_login.text = getString(R.string.general_loading)
            } else {
                btn_login.isEnabled = true
                btn_login.text = getString(R.string.general_loading)
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
                Status.ERROR -> container_password.error = it.data?.run { getString(this) }
                else -> container_password.isErrorEnabled = false
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
                ///TODO: Add intent to Home Activity
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