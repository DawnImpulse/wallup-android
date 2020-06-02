/**
 * ISC License
 *
 * Copyright 2020, Saksham (DawnImpulse)
 *
 * Permission to use, copy, modify, and/or distribute this software for any purpose with or without fee is hereby granted,
 * provided that the above copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING ALL
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS,
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE
 * OR PERFORMANCE OF THIS SOFTWARE.
 **/
package com.dawnimpulse.wallup.ui.activities

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.fragments.FragmentHome
import com.dawnimpulse.wallup.ui.fragments.FragmentLatestDevice
import com.dawnimpulse.wallup.ui.fragments.FragmentRandom
import com.dawnimpulse.wallup.ui.sheets.SheetUser
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_item.view.*
import kotlinx.android.synthetic.main.navigation.*

class ActivityMain : AppCompatActivity(R.layout.activity_main), View.OnClickListener {
    private lateinit var homeFragment: FragmentHome
    private lateinit var randomFragment: FragmentRandom
    private lateinit var latestDeviceFragment: FragmentLatestDevice
    private lateinit var sheetUser: SheetUser
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var currentNav = -1

    /**
     * on create (default)
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setNavigation()
        setupViewPager(activity_main_viewpager)
        sheetUser = SheetUser()
        F.nightMode()

        activity_main_appbar_device.setOnClickListener(this)
        activity_main_appbar_user.setOnClickListener(this)
    }

    /**
     * on resume
     */
    override fun onResume() {
        super.onResume()

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            window.decorView.systemUiVisibility = 0
        else
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    /**
     * on click listener
     */
    override fun onClick(v: View?) {
        v?.let {
            when (it.id) {
                activity_main_appbar_device.id -> openActivity(ActivityDevices::class.java)
                activity_main_appbar_user.id -> sheetUser.open(supportFragmentManager)
            }
        }
    }

    /**
     * set navigation items
     */
    private fun setNavigation() {

        // get list of icons & name
        val icons = listOf(R.drawable.vd_home, R.drawable.vd_rhombus, R.drawable.vd_random)
        val name = listOf("Home", "Device", "Random")

        // run a loop and set items
        for (i in 0..2) {
            // get inflated view
            val item = LayoutInflater.from(this).inflate(R.layout.nav_item, nav_layout, false)
            // set icon
            item.nav_item_icon.setImageDrawable(ContextCompat.getDrawable(this, icons[i]))
            item.nav_item_text.text = name[i] // set name
            // if first item then remove left margin
            if (i == 0) {
                val params = item.nav_item_container.layoutParams as ViewGroup.MarginLayoutParams
                params.leftMargin = 0
            }
            // add view to navigation
            nav_layout.addView(item)
            // enable click listener
            item.setOnClickListener {
                if (currentNav != i)
                    currentNavigation(i)
            }
        }

        // set first item
        currentNavigation(0)
    }

    /**
     * handle navigation
     *
     * @param pos
     */
    private fun currentNavigation(pos: Int) {
        if (currentNav != pos) {
            activity_main_viewpager.currentItem = pos
            val current = nav_layout.getChildAt(pos) // get current view
            val topBottom = dpToPx(10) // padding value
            var prevItem = AnimatorSet() // create prev animator set
            val currentName = current.nav_item_text.text // name of item

            // current animation set
            val newItem = AnimatorSet().apply {
                // padding animation
                play(ValueAnimator.ofInt(0, topBottom).apply {
                    // add listener to change padding gradually
                    addUpdateListener {
                        val value = it.animatedValue as Int
                        current.nav_item_container.setPadding(value + dpToPx(4), value, value + dpToPx(4), value)
                    }
                    // fading animation
                }).with(ValueAnimator.ofInt(0, currentName.length).apply {
                    // add listener to change length gradually
                    addUpdateListener {
                        current.nav_item_text.text = currentName.subSequence(0, it.animatedValue as Int)
                    }
                })

                doOnStart {
                    // on start show the text
                    current.nav_item_text.show()
                    current.nav_item_container.background = ContextCompat.getDrawable(this@ActivityMain,
                            R.drawable.bg_nav_selected)
                }
            }

            // work on the last item (if present)
            if (currentNav != -1) {
                val item = nav_layout.getChildAt(currentNav) // get prev item
                val prevName = item.nav_item_text.text // get prev item name
                prevItem = AnimatorSet().apply {
                    // shrink animation
                    play(ValueAnimator.ofInt(topBottom, 0).apply {
                        // listener to gradually change
                        addUpdateListener {
                            val value = it.animatedValue as Int
                            item.nav_item_container.setPadding(value)
                        }
                        // fadeout animation
                    }).with(ValueAnimator.ofInt(prevName.length, 0).apply {
                        // listener to gradually change
                        addUpdateListener {
                            item.nav_item_text.text = prevName.subSequence(0, it.animatedValue as Int)
                        }
                    })

                    doOnEnd {
                        item.nav_item_text.gone() // remove text
                        item.nav_item_container.background = null // remove bg
                        item.nav_item_text.text = prevName // restore name
                    }
                }
            }

            AnimatorSet().apply {
                play(newItem).with(prevItem) // join animations
                duration = 100 // set duration
                start() // start animation set
                doOnEnd {
                    currentNav = pos
                }
            }
        }
    }

    /**
     * setting up our viewpager
     *
     * @param viewPager
     */
    private fun setupViewPager(viewPager: ViewPager) {

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        homeFragment = FragmentHome()
        latestDeviceFragment = FragmentLatestDevice()
        randomFragment = FragmentRandom()

        pagerAdapter.addFragment(homeFragment, HOME)
        pagerAdapter.addFragment(latestDeviceFragment, DEVICE)
        pagerAdapter.addFragment(randomFragment, RANDOM)
        viewPager.adapter = pagerAdapter
        viewPager.offscreenPageLimit = 2

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //
            }

            override fun onPageSelected(position: Int) {
                currentNavigation(position)
                if (position == 1) {
                    activity_main_appbar_device.show()
                    activity_main_appbar_logo.gone()
                } else {
                    activity_main_appbar_device.gone()
                    activity_main_appbar_logo.show()
                }
            }
        })
    }
}