/*
ISC License

Copyright 2018-2019, Saksham (DawnImpulse)

Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
provided that the above copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
OR PERFORMANCE OF THIS SOFTWARE.*/
package org.sourcei.wallup.deprecated.activities

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.core.view.updateMargins
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_collection_layout.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.F
import org.sourcei.wallup.deprecated.utils.ViewPagerAdapter

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 15 - master - wallup collections
 * Saksham - 2018 11 28 - master - connection handling
 */
class CollectionLayoutActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "CollectionLayoutActivity"
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var generalFragment: org.sourcei.wallup.deprecated.fragments.CollectionFragment
    private lateinit var featuredFragment: org.sourcei.wallup.deprecated.fragments.CollectionFragment
    private lateinit var wallupFragment: org.sourcei.wallup.deprecated.fragments.CollectionFragment

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_layout)

        setupViewPager(colLViewPager)
        colNavFeaturedL.setOnClickListener(this)
        colNavGeneralL.setOnClickListener(this)
        colNavWallupL.setOnClickListener(this)
        colNavBack.setOnClickListener(this)

        colLViewPager.offscreenPageLimit = 2
        colLViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageSelected(position: Int) {
                currentNav(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                //
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //
            }
        })
        currentNav(0)
    }

    // on start
    override fun onStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this)
        super.onStart()
    }

    // on destroy
    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            colNavFeaturedL.id -> currentNav(0)
            colNavGeneralL.id -> currentNav(1)
            colNavWallupL.id -> currentNav(2)
            colNavBack.id -> finish()
        }
    }

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.NETWORK) {
                runOnUiThread {
                    val params = colLNav.layoutParams as CoordinatorLayout.LayoutParams
                    if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.NETWORK)) {
                        colLConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).GREEN)
                        colLConnText.text = "Back Online"
                        GlobalScope.launch {
                            delay(1500)
                            runOnUiThread {
                                colLConnLayout.visibility = View.GONE
                                params.updateMargins(bottom = 0)
                            }
                        }
                    } else {
                        params.updateMargins(bottom = F.dpToPx(16, this))
                        colLConnLayout.visibility = View.VISIBLE
                        colLConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).LIKE)
                        colLConnText.text = "No Internet"
                    }
                }
            }
        }
    }

    // setup view pager
    private fun setupViewPager(viewPager: ViewPager) {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        generalFragment = org.sourcei.wallup.deprecated.fragments.CollectionFragment()
        featuredFragment = org.sourcei.wallup.deprecated.fragments.CollectionFragment()
        wallupFragment = org.sourcei.wallup.deprecated.fragments.CollectionFragment()

        featuredFragment.arguments = bundleOf(Pair(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.FEATURED))
        generalFragment.arguments = bundleOf(Pair(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.CURATED))
        wallupFragment.arguments = bundleOf(Pair(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.WALLUP))

        pagerAdapter.addFragment(featuredFragment, org.sourcei.wallup.deprecated.utils.C.FEATURED)
        pagerAdapter.addFragment(generalFragment, org.sourcei.wallup.deprecated.utils.C.CURATED)
        pagerAdapter.addFragment(wallupFragment, org.sourcei.wallup.deprecated.utils.C.WALLUP)
        viewPager.adapter = pagerAdapter
    }

    // current nav position
    private fun currentNav(pos: Int) {
        var colors = org.sourcei.wallup.deprecated.utils.Colors(this)
        when (pos) {
            0 -> {
                colNavFeatured.setTypeface(null, BOLD)
                colNavGeneral.setTypeface(null, NORMAL)
                colNavWallup.setTypeface(null, NORMAL)

                colNavFeatured.setTextColor(colors.WHITE)
                colNavGeneral.setTextColor(colors.GREY_400)
                colNavWallup.setTextColor(colors.GREY_400)

                colNavFeatured.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                colNavGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colNavWallup.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colLViewPager.currentItem = 0
            }
            1 -> {
                colNavFeatured.setTypeface(null, NORMAL)
                colNavGeneral.setTypeface(null, BOLD)
                colNavWallup.setTypeface(null, NORMAL)

                colNavFeatured.setTextColor(colors.GREY_400)
                colNavGeneral.setTextColor(colors.WHITE)
                colNavWallup.setTextColor(colors.GREY_400)

                colNavFeatured.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                colNavGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)
                colNavWallup.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colLViewPager.currentItem = 1
            }
            2 -> {
                colNavFeatured.setTypeface(null, NORMAL)
                colNavGeneral.setTypeface(null, NORMAL)
                colNavWallup.setTypeface(null, BOLD)

                colNavFeatured.setTextColor(colors.GREY_400)
                colNavGeneral.setTextColor(colors.GREY_400)
                colNavWallup.setTextColor(colors.WHITE)

                colNavFeatured.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                colNavGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colNavWallup.setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)
                colLViewPager.currentItem = 2
            }
        }
    }
}
