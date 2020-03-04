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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.ui.fragments.FragmentRandom
import com.dawnimpulse.wallup.utils.reusables.RANDOM
import com.dawnimpulse.wallup.utils.reusables.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @info - application home-screen
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-04 by Saksham
 * @note Updates :
 */
class ActivityMain : AppCompatActivity() {
    private lateinit var randomFragment: FragmentRandom
    private lateinit var pagerAdapter: ViewPagerAdapter

    /**
     * on create (default)
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewPager(activity_main_viewpager)
    }

    /**
     * setting up our viewpager
     *
     * @param viewPager
     */
    private fun setupViewPager(viewPager: ViewPager) {

        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        randomFragment = FragmentRandom()

        pagerAdapter.addFragment(randomFragment, RANDOM)
        viewPager.adapter = pagerAdapter
    }
}