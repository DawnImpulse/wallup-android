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

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.toast
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.MainFragment
import com.dawnimpulse.wallup.sheets.ModalSheetNav
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Colors
import com.dawnimpulse.wallup.utils.RemoteConfig
import com.dawnimpulse.wallup.utils.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - recent
 * @note Created on 2018-05-15 by Saksham
 *
 * @note Updates :
 *  Saksham - 2018 05 20 - recent - adding multiple fragments
 *  Saksham - 2018 08 19 - master - using bottom modal sheet navigation
 *  Saksham - 2018 09 15 - master - remote config for update
 *  Saksham - 2018 10 10 - master - random fragment
 */
class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var randomFragment:MainFragment
    private lateinit var latestFragment: MainFragment
    private lateinit var trendingFragment: MainFragment
    private lateinit var curatedFragment: MainFragment
    private lateinit var navSheet: ModalSheetNav
    private lateinit var navBundle: Bundle
    private var lastItemSelected = 0

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(mainToolbar)

        navSheet = ModalSheetNav()
        navBundle = Bundle()
        navBundle.putInt(C.BOTTOM_SHEET, R.layout.bottom_sheet_navigation)
        navSheet.arguments = navBundle

        setupViewPager(mainViewPager)
        currentNavItem(0)
        mainViewPager.addOnPageChangeListener(this)
        mainViewPager.offscreenPageLimit = 2

        mainNavTrending.setOnClickListener(this)
        mainNavRandom.setOnClickListener(this)
        mainNavLatest.setOnClickListener(this)
        mainNavUp.setOnClickListener(this)
        mainRefresh.setOnClickListener(this)
        mainSearch.setOnClickListener(this)

        RemoteConfig.update()
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            mainNavLatest.id -> currentNavItem(0)
            mainNavRandom.id -> currentNavItem(1)
            mainNavTrending.id -> currentNavItem(2)
            mainNavUp.id -> {
                navSheet.show(supportFragmentManager, C.BOTTOM_SHEET)
                currentNavItem(lastItemSelected)
            }
            mainRefresh.id -> {
                when (lastItemSelected) {
                    0 -> {
                        toast("Refreshing Latest List")
                        latestFragment.onRefresh()
                    }
                    1 -> {
                        toast("Refreshing Random List")
                        randomFragment.onRefresh()
                    }
                    2 -> {
                        toast("Refreshing Trending List")
                        trendingFragment.onRefresh()
                    }
                }
            }
            mainSearch.id ->{
                startActivity(Intent(this,SearchActivity::class.java))
                overridePendingTransition(R.anim.enter_from_left,R.anim.fade_out)
            }
        }
    }

    // viewpager
    override fun onPageSelected(position: Int) {
        currentNavItem(position)
    }

    // viewpager dummy
    override fun onPageScrollStateChanged(state: Int) {
        //
    }

    // viewpager dummy
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        //
    }

    // Setup our viewpager
    private fun setupViewPager(viewPager: ViewPager) {
        val latestBundle = Bundle()
        val trendingBundle = Bundle()
        val randomBundle = Bundle()

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        latestFragment = MainFragment()
        randomFragment = MainFragment()
        trendingFragment = MainFragment()

        latestBundle.putString(C.TYPE, C.LATEST)
        randomBundle.putString(C.TYPE, C.RANDOM)
        trendingBundle.putString(C.TYPE, C.TRENDING)

        trendingBundle.putBoolean(C.LIKE,false)

        latestFragment.arguments = latestBundle
        randomFragment.arguments = randomBundle
        trendingFragment.arguments = trendingBundle

        pagerAdapter.addFragment(latestFragment, C.LATEST)
        pagerAdapter.addFragment(randomFragment, C.RANDOM)
        pagerAdapter.addFragment(trendingFragment, C.TRENDING)
        viewPager.adapter = pagerAdapter
    }

    // current nav item selected
    private fun currentNavItem(pos: Int) {
        changeNavColor(pos)
        when (pos) {
            0 -> {
                lastItemSelected = 0
                mainViewPager.currentItem = 0
                mainNavLatestT.visibility = View.VISIBLE
                mainNavRandomT.visibility = View.GONE
                mainNavTrendingT.visibility = View.GONE
            }
            1 -> {
                lastItemSelected = 1
                mainViewPager.currentItem = 1
                mainNavLatestT.visibility = View.GONE
                mainNavRandomT.visibility = View.VISIBLE
                mainNavTrendingT.visibility = View.GONE
            }
            2 -> {
                lastItemSelected = 2
                mainViewPager.currentItem = 2
                mainNavLatestT.visibility = View.GONE
                mainNavRandomT.visibility = View.GONE
                mainNavTrendingT.visibility = View.VISIBLE
            }
        }
    }

    // change nav icon colors
    private fun changeNavColor(pos: Int) {
        val colors = Colors(this)
        when (pos) {
            0 -> {
                mainNavLatestI.drawable.setColorFilter(colors.BLACK, PorterDuff.Mode.SRC_ATOP)
                mainNavRandomI.drawable.setColorFilter(colors.GREY_400,PorterDuff.Mode.SRC_ATOP)
                mainNavTrendingI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
            }
            1 -> {
                mainNavLatestI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
                mainNavRandomI.drawable.setColorFilter(colors.BLACK,PorterDuff.Mode.SRC_ATOP)
                mainNavTrendingI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
            }
            2 -> {
                mainNavLatestI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
                mainNavRandomI.drawable.setColorFilter(colors.GREY_400,PorterDuff.Mode.SRC_ATOP)
                mainNavTrendingI.drawable.setColorFilter(colors.BLACK, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
}
