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

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.sourcei.wallup.deprecated.R
import org.sourcei.wallup.deprecated.utils.*

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
 *  Saksham - 2018 11 24 - master - user icon on toolbar
 *  Saksham - 2018 11 28 - master - connection handling
 *  Saksham - 2018 12 03 - master - changes to toolbar & bottom navigation
 *  Saksham - 2018 12 19 - master - user sheet
 */
class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var randomFragment: org.sourcei.wallup.deprecated.fragments.MainFragment
    private lateinit var latestFragment: org.sourcei.wallup.deprecated.fragments.MainFragment
    private lateinit var navSheet: org.sourcei.wallup.deprecated.sheets.ModalSheetNav
    private lateinit var unsplashSheet: org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash
    private lateinit var userSheet: org.sourcei.wallup.deprecated.sheets.ModalSheetUser
    private lateinit var navBundle: Bundle
    private var lastItemSelected = 0

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(mainToolbar)

        help()

        navSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetNav()
        unsplashSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetUnsplash()
        userSheet = org.sourcei.wallup.deprecated.sheets.ModalSheetUser()
        navBundle = Bundle()
        navBundle.putInt(org.sourcei.wallup.deprecated.utils.C.BOTTOM_SHEET, R.layout.bottom_sheet_navigation)
        navSheet.arguments = navBundle

        setupViewPager(mainViewPager)
        currentNavItem(0)
        mainViewPager.addOnPageChangeListener(this)
        mainViewPager.offscreenPageLimit = 2

        mainNavRandom.setOnClickListener(this)
        mainNavLatest.setOnClickListener(this)
        mainNavUp.setOnClickListener(this)
        mainUser.setOnClickListener(this)
        mainSearch.setOnClickListener(this)

        RemoteConfig.update()

        mainUser.setOnLongClickListener {
            toast("user profile with Unsplash")
            true
        }
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

    // on resume
    // - used for handling user icon
    override fun onResume() {
        super.onResume()
        if (Prefs.contains(org.sourcei.wallup.deprecated.utils.C.USER)) {
            var user = Gson().fromJson(Prefs.getString(org.sourcei.wallup.deprecated.utils.C.USER, ""), org.sourcei.wallup.deprecated.pojo.UserPojo::class.java)
            org.sourcei.wallup.deprecated.handlers.ImageHandler.setImageInView(lifecycle, mainUserI, user.profile_image.large)
            mainUserI.borderColor = org.sourcei.wallup.deprecated.utils.Colors(this).WHITE
            mainUserI.borderWidth = F.dpToPx(1, this).toFloat()
        } else {
            mainUserI.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.vd_user_outline))
            mainUserI.borderWidth = 0f
        }

    }

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            mainNavLatest.id -> currentNavItem(0)
            mainNavRandom.id -> currentNavItem(1)
            mainNavUp.id -> {
                navSheet.show(supportFragmentManager, org.sourcei.wallup.deprecated.utils.C.BOTTOM_SHEET)
                currentNavItem(lastItemSelected)
            }
            mainUser.id -> {
                if (!Prefs.contains(org.sourcei.wallup.deprecated.utils.C.USER_TOKEN))
                    unsplashSheet.show(supportFragmentManager, unsplashSheet.tag)
                else
                    userSheet.show(supportFragmentManager)
            }
            /*mainRefresh.id -> {
                when (lastItemSelected) {
                    0 -> {
                        toast("Refreshing Latest List")
                        latestFragment.onRefresh()
                    }
                    1 -> {
                        toast("Refreshing Random List")
                        randomFragment.onRefresh()
                    }
                }
            }*/
            mainSearch.id -> {
                startActivity(Intent(this, org.sourcei.wallup.deprecated.activities.SearchActivity::class.java))
                overridePendingTransition(R.anim.enter_from_left, R.anim.fade_out)
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

    // on message event
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: org.sourcei.wallup.deprecated.utils.Event) {
        if (event.obj.has(org.sourcei.wallup.deprecated.utils.C.TYPE)) {
            if (event.obj.getString(org.sourcei.wallup.deprecated.utils.C.TYPE) == org.sourcei.wallup.deprecated.utils.C.NETWORK) {
                runOnUiThread {
                    val params = mainNav.layoutParams as CoordinatorLayout.LayoutParams
                    if (event.obj.getBoolean(org.sourcei.wallup.deprecated.utils.C.NETWORK)) {
                        mainConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).GREEN)
                        mainConnText.text = "Back Online"
                        GlobalScope.launch {
                            delay(1500)
                            runOnUiThread {
                                mainConnLayout.visibility = View.GONE
                                params.updateMargins(bottom = 0)
                            }
                        }
                    } else {
                        params.updateMargins(bottom = F.dpToPx(16, this))
                        mainConnLayout.visibility = View.VISIBLE
                        mainConnLayout.setBackgroundColor(org.sourcei.wallup.deprecated.utils.Colors(this).LIKE)
                        mainConnText.text = "No Internet"
                    }
                }
            }
        }
    }

    // Setup our viewpager
    private fun setupViewPager(viewPager: ViewPager) {
        val latestBundle = Bundle()
        val trendingBundle = Bundle()
        val randomBundle = Bundle()

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        latestFragment = org.sourcei.wallup.deprecated.fragments.MainFragment()
        randomFragment = org.sourcei.wallup.deprecated.fragments.MainFragment()

        latestBundle.putString(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.LATEST)
        randomBundle.putString(org.sourcei.wallup.deprecated.utils.C.TYPE, org.sourcei.wallup.deprecated.utils.C.RANDOM)

        latestFragment.arguments = latestBundle
        randomFragment.arguments = randomBundle

        pagerAdapter.addFragment(latestFragment, org.sourcei.wallup.deprecated.utils.C.LATEST)
        pagerAdapter.addFragment(randomFragment, org.sourcei.wallup.deprecated.utils.C.RANDOM)
        viewPager.adapter = pagerAdapter
    }

    // current nav item selected
    private fun currentNavItem(pos: Int) {
        val white = org.sourcei.wallup.deprecated.utils.Colors(this).WHITE
        val accent = org.sourcei.wallup.deprecated.utils.Colors(this).ACCENT
        when (pos) {
            0 -> {
                lastItemSelected = 0
                mainViewPager.currentItem = 0
                mainNavLatestI.setImageDrawable(org.sourcei.wallup.deprecated.utils.Drawables(this).latest)
                mainNavRandomI.setImageDrawable(org.sourcei.wallup.deprecated.utils.Drawables(this).shuffle1)
                mainNavLatestT.setTextColor(accent)
                mainNavRandomT.setTextColor(white)
                /*mainNavLatestT.typeface = Typeface.DEFAULT_BOLD
                mainNavRandomT.typeface = Typeface.DEFAULT
                mainNavLatestT.textSize = 14f
                mainNavRandomT.textSize = 12f*/
            }
            1 -> {
                lastItemSelected = 1
                mainViewPager.currentItem = 1
                mainNavLatestI.setImageDrawable(org.sourcei.wallup.deprecated.utils.Drawables(this).latest_outline)
                mainNavRandomI.setImageDrawable(org.sourcei.wallup.deprecated.utils.Drawables(this).shuffle2)
                mainNavLatestT.setTextColor(white)
                mainNavRandomT.setTextColor(accent)
                /*mainNavLatestT.typeface = Typeface.DEFAULT
                mainNavRandomT.typeface = Typeface.DEFAULT_BOLD
                mainNavLatestT.textSize = 12f
                mainNavRandomT.textSize = 14f*/
            }
        }
    }

    //help section
    private fun help() {
        if (!Prefs.contains(org.sourcei.wallup.deprecated.utils.C.HELP_MAIN)) {
            showTarget(this,
                    wallup,
                    "Welcome",
                    "Click on an image to view its details.\nLong press image for common options"
            ) {
                showTarget(this,
                        mainUser,
                        "Unsplash User Profile",
                        "Manage your unsplash user profile"
                ) {
                    showTarget(this,
                            mainSearch,
                            "Search a wide variety of images on Unsplash.",
                            ""
                    ) {
                        showTarget(this,
                                mainNavUp,
                                "Menu",
                                "Tap to open bottom navigation"
                        ) {
                            Prefs.putBoolean(org.sourcei.wallup.deprecated.utils.C.HELP_MAIN, true)
                        }
                    }
                }
            }
        }
    }
}
