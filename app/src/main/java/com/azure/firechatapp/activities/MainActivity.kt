package com.azure.firechatapp.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.azure.firechatapp.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth.signOut()
    }
}
