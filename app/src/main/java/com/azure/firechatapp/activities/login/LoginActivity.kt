package com.azure.firechatapp.activities.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.R
import com.azure.firechatapp.goToActivity
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

            if(isValidEmailAndPassword(email,password)){
                logInByEmail(email,password)
            }
        }

        textViewForgotPassword.setOnClickListener { goToActivity<ForgotPasswordActivity>() }
        buttonCreateAccount.setOnClickListener { goToActivity<SignUpActivity>() }
    }

    private fun logInByEmail(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){

                }else{

                }
            }
    }

    private fun isValidEmailAndPassword(email: String, password: String): Boolean{
        return !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }
}
