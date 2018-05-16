package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.MainFragment
import com.dawnimpulse.wallup.utils.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var pagerAdapter: ViewPagerAdapter
    lateinit var mainFragment: MainFragment

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager(mainViewPager)
        navigation.setOnNavigationItemSelectedListener(this)
    }

    /**
     * On navigation item selected
     */
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                return true
            }
            R.id.navigation_dashboard -> {
                return true
            }
            R.id.navigation_notifications -> {
                return true
            }
        }
        return false
    }

    /**
     * Setup our viewpager
     */
    private fun setupViewPager(viewPager: ViewPager) {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        mainFragment = MainFragment()

        pagerAdapter.addFragment(mainFragment, "Latest")
        viewPager.adapter = pagerAdapter

    }
}
