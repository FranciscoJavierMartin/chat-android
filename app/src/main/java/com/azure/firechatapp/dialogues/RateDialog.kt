package com.azure.firechatapp.dialogues

import android.support.v7.app.AlertDialog
import android.support.v4.app.DialogFragment

import android.app.Dialog
import android.os.Bundle
import com.azure.firechatapp.R
import com.azure.firechatapp.models.NewRateEvent
import com.azure.firechatapp.models.Rate
import com.azure.firechatapp.toast
import com.azure.firechatapp.utils.RxBus
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.Date

class RateDialog: DialogFragment(){

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var currentUser: FirebaseUser

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        setUpCurrentUser()
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_rate, null)

        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)){ _, _ ->
                val textRate = view.editTextRateFeedback.text.toString()
                if(textRate.isNotEmpty()){
                    val imgURL = currentUser.photoUrl?.toString() ?: run {""}
                    val rate = Rate(currentUser.uid,textRate, view.ratingBarFeedback.rating, Date(), imgURL)
                    RxBus.publish(NewRateEvent(rate))
                } else {
                    // TODO: Create a better response
                    activity!!.toast("Please fill all fields")
                }

            }.setNegativeButton(getString(R.string.dialog_cancel)){ _, _ ->
                activity!!.toast("Pressed Cancel")
            }.create()
    }

    private fun setUpCurrentUser() {
        currentUser = mAuth.currentUser!!
    }
}

