package com.azure.firechatapp.activities.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.R
import com.azure.firechatapp.goToActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        buttonGoLogin.setOnClickListener {
            goToActivity<LoginActivity>()
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
    }
}
