package com.azure.firechatapp.activities.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.*
import com.azure.firechatapp.activities.MainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {


    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleApiClient by lazy { getGoogleApiClient() }
    private val REQUEST_CODE_GOOGLE_SIGN_IN = 99

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

        buttonLogInGoogle.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_GOOGLE_SIGN_IN){
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess){
                val account = result.signInAccount
                logInByGoogleAccountIntoFirebase(account !!)
            }
        }
    }

    private fun logInByGoogleAccountIntoFirebase(googleAccount: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(googleAccount.idToken, null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(this){
            if(mGoogleApiClient.isConnected){
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            }

            goToActivity<MainActivity> {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

        }
    }

    private fun logInByEmail(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){ task ->
                if(task.isSuccessful){

                    if(mAuth.currentUser!!.isEmailVerified){
                        goToActivity<MainActivity> {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
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
