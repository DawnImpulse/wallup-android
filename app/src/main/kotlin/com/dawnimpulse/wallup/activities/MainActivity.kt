/*
ISC License

Copyright 2018, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package com.dawnimpulse.wallup.activities

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.MainFragment
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.ViewPagerAdapter
import it.sephiroth.android.library.bottomnavigation.BottomNavigation
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-15 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 20 - recent - adding multiple fragments
 */
class MainActivity : AppCompatActivity(), BottomNavigation.OnMenuItemSelectionListener,ViewPager.OnPageChangeListener {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var latestFragment: MainFragment
    private lateinit var trendingFragment: MainFragment
    private lateinit var curatedFragment: MainFragment

    /**
     * On create
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager(mainViewPager)
        navigation.setOnMenuItemClickListener(this)
        mainViewPager.addOnPageChangeListener(this)
    }

    /**
     * On menu item select
     */
    override fun onMenuItemSelect(p0: Int, position: Int, p2: Boolean) {
        if (position != 3)
            mainViewPager.currentItem = position
    }

    /**
     * On menu item reselect
     */
    override fun onMenuItemReselect(p0: Int, p1: Int, p2: Boolean) {

    }

    /**
     * Setup our viewpager
     */
    private fun setupViewPager(viewPager: ViewPager) {
        val latestBundle = Bundle()
        val trendingBundle = Bundle()
        val curatedBundle = Bundle()

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        latestFragment = MainFragment()
        trendingFragment = MainFragment()
        curatedFragment = MainFragment()

        latestBundle.putString(C.TYPE, C.LATEST)
        trendingBundle.putString(C.TYPE, C.TRENDING)
        curatedBundle.putString(C.TYPE, C.CURATED)

        latestFragment.arguments = latestBundle
        trendingFragment.arguments = trendingBundle
        curatedFragment.arguments = curatedBundle

        pagerAdapter.addFragment(latestFragment, C.LATEST)
        pagerAdapter.addFragment(trendingFragment, C.TRENDING)
        pagerAdapter.addFragment(curatedFragment, C.CURATED)
        viewPager.adapter = pagerAdapter
    }


    /**
     * On page selected (viewpager)
     */
    override fun onPageSelected(position: Int) {
        navigation.setSelectedIndex(position,true)
    }

    /**
     * On page scroll state (viewpager)
     */
    override fun onPageScrollStateChanged(state: Int) {
        //
    }

    /**
     * On page scrolled (viewpager)
     */
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //
    }
}
