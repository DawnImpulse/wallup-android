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
package com.dawnimpulse.wallup.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.IntroInfoFragment
import com.dawnimpulse.wallup.fragments.IntroUnsplashFragment
import com.dawnimpulse.wallup.fragments.IntroWelcomeFragment
import com.dawnimpulse.wallup.utils.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_intro.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-12-15 by Saksham
 *
 * @note Updates :
 */
class IntroActivity : AppCompatActivity() {
    private val NAME = "IntroActivity"
    private lateinit var welcome: IntroWelcomeFragment
    private lateinit var unsplashFragment: IntroUnsplashFragment
    private lateinit var infoFragment: IntroInfoFragment
    private lateinit var pagerAdapter: ViewPagerAdapter
    private var current = 0


    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setupViewPager(introViewPager)

        introFab.setOnClickListener {
            if (current == 2) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                current++
                introViewPager.currentItem = current
            }
        }
    }

    // setup viewpager
    private fun setupViewPager(viewPager: ViewPager) {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        welcome = IntroWelcomeFragment()
        unsplashFragment = IntroUnsplashFragment()
        infoFragment = IntroInfoFragment()

        pagerAdapter.addFragment(welcome, "")
        pagerAdapter.addFragment(unsplashFragment, "")
        pagerAdapter.addFragment(infoFragment, "")
        viewPager.adapter = pagerAdapter

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                //
            }

            override fun onPageSelected(position: Int) {
                current = position
                if (current == 2)
                    introFab.setImageDrawable(ContextCompat.getDrawable(this@IntroActivity, R.drawable.vd_check))
                else
                    introFab.setImageDrawable(ContextCompat.getDrawable(this@IntroActivity, R.drawable.vd_right_black))
            }

        })
    }
}
