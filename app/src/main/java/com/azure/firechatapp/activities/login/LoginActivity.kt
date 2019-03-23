package com.azure.firechatapp.activities.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }

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

    override fun onConnectionFailed(p0: ConnectionResult) {
        toast(R.string.connection_failed)
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

    private fun getGoogleApiClient(): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        return GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }
}
