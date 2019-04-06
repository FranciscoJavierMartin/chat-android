package com.azure.firechatapp.dialogues

import android.support.v7.app.AlertDialog
import android.support.v4.app.DialogFragment

import android.app.Dialog
import android.os.Bundle
import com.azure.firechatapp.R
import com.azure.firechatapp.toast

class RateDialog: DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context!!)
            .setTitle(getString(R.string.dialog_title))
            .setView(R.layout.dialog_rate)
            .setPositiveButton(getString(R.string.dialog_ok)){ dialog, which ->
                activity!!.toast("Pressed OK")
            }.setNegativeButton(getString(R.string.dialog_cancel)){ dialog, which ->
                activity!!.toast("Pressed Cancel")
            }.create()
    }
}

