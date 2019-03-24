package com.azure.firechatapp.activities

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.azure.firechatapp.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbarView as Toolbar)
    }
}
