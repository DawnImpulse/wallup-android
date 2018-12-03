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
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updateMargins
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.MainFragment
import com.dawnimpulse.wallup.handlers.ImageHandler
import com.dawnimpulse.wallup.pojo.UserPojo
import com.dawnimpulse.wallup.sheets.ModalSheetNav
import com.dawnimpulse.wallup.sheets.ModalSheetUnsplash
import com.dawnimpulse.wallup.utils.*
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

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
 */
class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, View.OnClickListener {
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var randomFragment: MainFragment
    private lateinit var latestFragment: MainFragment
    private lateinit var navSheet: ModalSheetNav
    private lateinit var userSheet: ModalSheetUnsplash
    private lateinit var navBundle: Bundle
    private var lastItemSelected = 0

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(mainToolbar)

        navSheet = ModalSheetNav()
        userSheet = ModalSheetUnsplash()
        navBundle = Bundle()
        navBundle.putInt(C.BOTTOM_SHEET, R.layout.bottom_sheet_navigation)
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
        if (Prefs.contains(C.USER)) {
            var user = Gson().fromJson(Prefs.getString(C.USER, ""), UserPojo::class.java)
            ImageHandler.setImageInView(lifecycle, mainUserI, user.profile_image.large)
            mainUserI.borderColor = Colors(this).WHITE
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
                navSheet.show(supportFragmentManager, C.BOTTOM_SHEET)
                currentNavItem(lastItemSelected)
            }
            mainUser.id -> {
                if (!Prefs.contains(C.USER_TOKEN))
                    userSheet.show(supportFragmentManager, userSheet.tag)
                else
                    startActivity(Intent(this, UserActivity::class.java))
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
                startActivity(Intent(this, SearchActivity::class.java))
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
    fun onEvent(event: Event) {
        if (event.obj.has(C.TYPE)) {
            if (event.obj.getString(C.TYPE) == C.NETWORK) {
                runOnUiThread {
                    val params = mainNav.layoutParams as CoordinatorLayout.LayoutParams
                    if (event.obj.getBoolean(C.NETWORK)) {
                        mainConnLayout.setBackgroundColor(Colors(this).GREEN)
                        mainConnText.text = "Back Online"
                        launch {
                            delay(1500)
                            runOnUiThread {
                                mainConnLayout.visibility = View.GONE
                                params.updateMargins(bottom = 0)
                            }
                        }
                    } else {
                        params.updateMargins(bottom = F.dpToPx(16, this))
                        mainConnLayout.visibility = View.VISIBLE
                        mainConnLayout.setBackgroundColor(Colors(this).LIKE)
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
        latestFragment = MainFragment()
        randomFragment = MainFragment()

        latestBundle.putString(C.TYPE, C.LATEST)
        randomBundle.putString(C.TYPE, C.RANDOM)

        latestFragment.arguments = latestBundle
        randomFragment.arguments = randomBundle

        pagerAdapter.addFragment(latestFragment, C.LATEST)
        pagerAdapter.addFragment(randomFragment, C.RANDOM)
        viewPager.adapter = pagerAdapter
    }

    // current nav item selected
    private fun currentNavItem(pos: Int) {
        when (pos) {
            0 -> {
                lastItemSelected = 0
                mainViewPager.currentItem = 0
                mainNavLatestI.setImageDrawable(Drawables(this).latest)
                mainNavRandomI.setImageDrawable(Drawables(this).shuffle1)
                mainNavLatestT.typeface = Typeface.DEFAULT_BOLD
                mainNavRandomT.typeface = Typeface.DEFAULT
                mainNavLatestT.textSize = 14f
                mainNavRandomT.textSize = 12f
            }
            1 -> {
                lastItemSelected = 1
                mainViewPager.currentItem = 1
                mainNavLatestI.setImageDrawable(Drawables(this).latest_outline)
                mainNavRandomI.setImageDrawable(Drawables(this).shuffle2)
                mainNavLatestT.typeface = Typeface.DEFAULT
                mainNavRandomT.typeface = Typeface.DEFAULT_BOLD
                mainNavLatestT.textSize = 12f
                mainNavRandomT.textSize = 14f
            }
        }
    }
}
