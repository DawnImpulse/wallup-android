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

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import androidx.core.widget.toast
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.MainFragment
import com.dawnimpulse.wallup.sheets.ModalSheetNav
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Colors
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
 */
class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {
    private lateinit var pagerAdapter: ViewPagerAdapter
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
        mainNavCurated.setOnClickListener(this)
        mainNavLatest.setOnClickListener(this)
        mainNavUp.setOnClickListener(this)
        mainRefresh.setOnClickListener(this)
        //ImageHandler.setImageInView(lifecycle, mainProfile, Config.TEMP_IMAGE)
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            mainNavTrending.id -> currentNavItem(0)
            mainNavLatest.id -> currentNavItem(1)
            mainNavCurated.id -> currentNavItem(2)
            mainNavUp.id -> {
                navSheet.show(supportFragmentManager, C.BOTTOM_SHEET)
                currentNavItem(lastItemSelected)
            }
            mainRefresh.id -> {
                when (lastItemSelected) {
                    0 -> {
                        toast("Refreshing Trending List")
                        trendingFragment.onRefresh()
                    }
                    1 -> {
                        toast("Refreshing Latest List")
                        latestFragment.onRefresh()
                    }
                    2 -> {
                        toast("Refreshing Curated List")
                        curatedFragment.onRefresh()
                    }
                }
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

        pagerAdapter.addFragment(trendingFragment, C.TRENDING)
        pagerAdapter.addFragment(latestFragment, C.LATEST)
        pagerAdapter.addFragment(curatedFragment, C.CURATED)
        viewPager.adapter = pagerAdapter
    }

    /**
     * current nav item selected
     * @param pos
     */
    private fun currentNavItem(pos: Int) {
        changeNavColor(pos)
        when (pos) {
            0 -> {
                lastItemSelected = 0
                mainViewPager.currentItem = 0
                mainNavTrendingT.visibility = View.VISIBLE
                mainNavLatestT.visibility = View.GONE
                mainNavCuratedT.visibility = View.GONE
            }
            1 -> {
                lastItemSelected = 1
                mainViewPager.currentItem = 1
                mainNavTrendingT.visibility = View.GONE
                mainNavLatestT.visibility = View.VISIBLE
                mainNavCuratedT.visibility = View.GONE
            }
            2 -> {
                lastItemSelected = 2
                mainViewPager.currentItem = 2
                mainNavTrendingT.visibility = View.GONE
                mainNavLatestT.visibility = View.GONE
                mainNavCuratedT.visibility = View.VISIBLE
            }
        }
    }

    /**
     * change nav icon colors
     */
    private fun changeNavColor(pos: Int) {
        val colors = Colors(this)
        when (pos) {
            0 -> {
                mainNavTrendingI.drawable.setColorFilter(colors.BLACK, PorterDuff.Mode.SRC_ATOP)
                mainNavLatestI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
                mainNavCuratedI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
            }
            1 -> {
                mainNavTrendingI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
                mainNavLatestI.drawable.setColorFilter(colors.BLACK, PorterDuff.Mode.SRC_ATOP)
                mainNavCuratedI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
            }
            2 -> {
                mainNavTrendingI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
                mainNavLatestI.drawable.setColorFilter(colors.GREY_400, PorterDuff.Mode.SRC_ATOP)
                mainNavCuratedI.drawable.setColorFilter(colors.BLACK, PorterDuff.Mode.SRC_ATOP)
            }
        }
    }
}
