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

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.fragments.FragmentHome
import com.dawnimpulse.wallup.ui.fragments.FragmentRandom
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.inflate_nav.view.*

/**
 * @info - application home-screen
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-04 by Saksham
 * @note Updates :
 */
class ActivityMain : AppCompatActivity(R.layout.activity_main) {
    private lateinit var randomFragment: FragmentRandom
    private lateinit var homeFragment: FragmentHome
    private lateinit var pagerAdapter: ViewPagerAdapter

    /**
     * on create (default)
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        setupViewPager(activity_main_viewpager)
        F.nightMode()
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
     * setup navigation
     */
    private fun setupNavigation() {
        val logos = listOf(R.drawable.vd_home, R.drawable.vd_random, R.drawable.vd_like_1)
        for (i in 0..2) {
            val item = LayoutInflater.from(this).inflate(R.layout.inflate_nav, navigation, false)
            item.inflate_nav_logo.setImageDrawable(ContextCompat.getDrawable(this, logos[i]))
            if (i != 0) {
                val params = item.inflate_nav_logo.layoutParams
                val dimen = dpToPx(30)
                params.height = dimen
                params.width = dimen
            }
            item.setOnClickListener {
                selectNavigation(i)
            }
            navigation.addView(item)
        }
        selectNavigation(0)
    }

    /**
     * select navigation item
     *
     * @param pos
     */
    private fun selectNavigation(pos: Int) {
        activity_main_viewpager.currentItem = pos
        val items = (0..2).toMutableList()
        items.removeAt(pos)
        ImageViewCompat.setImageTintList(navigation.getChildAt(pos).inflate_nav_logo, ColorStateList.valueOf(Colors.ACCENT));
        for (i in items) {
            ImageViewCompat.setImageTintList(navigation.getChildAt(i).inflate_nav_logo, ColorStateList.valueOf(Colors.WHITE));
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
        randomFragment = FragmentRandom()

        pagerAdapter.addFragment(homeFragment, HOME)
        pagerAdapter.addFragment(randomFragment, RANDOM)
        viewPager.adapter = pagerAdapter
    }
}