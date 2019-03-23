package com.azure.firechatapp.activities.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextEmail.validate {
            editTextEmail.error =
                if(isValidEmail(it)) null
                else resources.getString(R.string.login_error_on_email)
        }

        buttonGoLogin.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        buttonForgot.setOnClickListener {
            val email = editTextEmail.text.toString()

            if(isValidEmail(email)){
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    toast(R.string.login_email_reset)

                    goToActivity<LoginActivity> {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }

                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                }

            }else{
                toast(resources.getString(R.string.login_email_address_incorrect))
            }

        }

    }
}
