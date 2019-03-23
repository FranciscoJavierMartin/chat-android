package com.azure.firechatapp.activities.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.azure.firechatapp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        buttonGoLogin.setOnClickListener {
            goToActivity<LoginActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }

        buttonSignUp.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            if(isValidEmail(email) && isValidPassword(password) && isValidConfirmPassword(password, confirmPassword)){
                signUpByEmail(email,password)
            }else{
                toast(R.string.login_fill_all_data)
            }
        }

        editTextEmail.validate {
            editTextEmail.error =
                if(isValidEmail(it)) null
                else resources.getString(R.string.login_error_on_email)

        }

        editTextPassword.validate {
            editTextPassword.error =
                if(isValidPassword(it)) null
                else resources.getString(R.string.login_error_on_password)
        }

        editTextConfirmPassword.validate {
            editTextConfirmPassword.error =
                if(isValidConfirmPassword(editTextPassword.text.toString(), it)) null
                else resources.getString(R.string.login_error_on_confirm_password)
        }


    }

    private fun signUpByEmail(email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener(this){
                        toast(R.string.login_email_confirmation_sent)
                        goToActivity<LoginActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }else{
                    toast(R.string.login_please_try_again)
                }
            }
    }



}
