package com.azure.firechatapp.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.azure.firechatapp.interfaces.IToolbar

open class ToolbarActivity: AppCompatActivity(), IToolbar {

    protected var _toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun toolbarToLoad(toolbar: Toolbar?) {
        _toolbar = toolbar;
        _toolbar?.let { setSupportActionBar(_toolbar) }
    }

    override fun enableHomeDisplay(value: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(value)
    }

}