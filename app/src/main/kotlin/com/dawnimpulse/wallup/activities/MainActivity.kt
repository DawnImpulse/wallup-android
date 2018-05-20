package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.MainFragment
import com.dawnimpulse.wallup.utils.ViewPagerAdapter
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigation.OnMenuItemSelectionListener {
    override fun onMenuItemSelect(p0: Int, p1: Int, p2: Boolean) {

    }

    override fun onMenuItemReselect(p0: Int, p1: Int, p2: Boolean) {

    }

    lateinit var pagerAdapter: ViewPagerAdapter
    lateinit var mainFragment: MainFragment

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager(mainViewPager)
        navigation.setOnMenuItemClickListener(this)
        //navigation.setOnNavigationItemSelectedListener(this)
    }

    /**
     * On navigation item selected
     */
    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
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
    }*/

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
