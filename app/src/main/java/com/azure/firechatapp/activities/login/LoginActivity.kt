package com.azure.firechatapp.activities.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(mAuth.currentUser === null){

        }else{
            mAuth.signOut()
        }

        buttonLogIn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if(isValidEmail(email) && isValidPassword(password)){
                logInByEmail(email,password)
            }else{
                toast(R.string.login_fill_all_data)
            }
        }

        textViewForgotPassword.setOnClickListener {
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        }
        buttonCreateAccount.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
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

    }

    private fun logInByEmail(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){

                    if(mAuth.currentUser!!.isEmailVerified){
                        toast(R.string.login_user_is_logged)
                    }else{
                        toast(R.string.login_email_confirmation_sent)
                    }

                }else{

                }
            }
    }

}
