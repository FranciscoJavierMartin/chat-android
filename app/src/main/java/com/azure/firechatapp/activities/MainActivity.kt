package com.azure.firechatapp.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.azure.firechatapp.R
import com.azure.firechatapp.adapters.PagerAdapter
import com.azure.firechatapp.fragments.ChatFragment
import com.azure.firechatapp.fragments.InfoFragment
import com.azure.firechatapp.fragments.RatesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ToolbarActivity() {

    private var prevBottomSelected: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbarToLoad(toolbarView as Toolbar)

        setUpViewPager(getPagerAdapter())
        setUpBottomNavigationBar()
    }

    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(ChatFragment())
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        return adapter
    }

    private fun setUpViewPager(adapter: PagerAdapter){
        viewPager.adapter = adapter
        // Keep in memory 3 tabs. Use with caution
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if(prevBottomSelected == null){
                    bottomNavigation.menu.getItem(0).isChecked = false
                }else{
                    prevBottomSelected!!.isChecked = false
                }
                bottomNavigation.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNavigation.menu.getItem(position)
            }

        })
    }

    private fun setUpBottomNavigationBar() {
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.bottom_nav_info -> {
                    viewPager.currentItem = 0;true
                }

                R.id.bottom_nav_rates -> {
                    viewPager.currentItem = 1;true
                }

                R.id.bottom_nav_chat -> {
                    viewPager.currentItem = 2;true
                }
                else -> false

            }
        }
    }
}
