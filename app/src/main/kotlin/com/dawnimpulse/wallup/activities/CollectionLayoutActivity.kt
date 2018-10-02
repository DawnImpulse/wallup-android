package com.dawnimpulse.wallup.activities

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.viewpager.widget.ViewPager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.fragments.CollectionFragment
import com.dawnimpulse.wallup.utils.C
import com.dawnimpulse.wallup.utils.Colors
import com.dawnimpulse.wallup.utils.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_collection_layout.*

/**
 * @author Saksham
 *
 * @note Last Branch Update - master
 * @note Created on 2018-09-08 by Saksham
 *
 * @note Updates :
 * Saksham - 2018 09 15 - master - wallup collections
 */
class CollectionLayoutActivity : AppCompatActivity(), View.OnClickListener {
    private val NAME = "CollectionLayoutActivity"
    private lateinit var pagerAdapter: ViewPagerAdapter
    private lateinit var generalFragment: CollectionFragment
    private lateinit var featuredFragment: CollectionFragment
    private lateinit var wallupFragment: CollectionFragment

    // on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collection_layout)

        setupViewPager(colLViewPager)
        colNavFeaturedL.setOnClickListener(this)
        colNavGeneralL.setOnClickListener(this)
        colNavWallupL.setOnClickListener(this)

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

    // on click
    override fun onClick(v: View) {
        when (v.id) {
            colNavFeaturedL.id -> currentNav(0)
            colNavGeneralL.id -> currentNav(1)
            colNavWallupL.id -> currentNav(2)
        }
    }

    // setup view pager
    private fun setupViewPager(viewPager: ViewPager) {
        pagerAdapter = ViewPagerAdapter(supportFragmentManager)
        generalFragment = CollectionFragment()
        featuredFragment = CollectionFragment()
        wallupFragment = CollectionFragment()

        featuredFragment.arguments = bundleOf(Pair(C.TYPE, C.FEATURED))
        generalFragment.arguments = bundleOf(Pair(C.TYPE, C.CURATED))
        wallupFragment.arguments = bundleOf(Pair(C.TYPE, C.WALLUP))

        pagerAdapter.addFragment(featuredFragment, C.FEATURED)
        pagerAdapter.addFragment(generalFragment, C.CURATED)
        pagerAdapter.addFragment(wallupFragment, C.WALLUP)
        viewPager.adapter = pagerAdapter
    }

    // current nav position
    private fun currentNav(pos: Int) {
        var colors = Colors(this)
        when (pos) {
            0 -> {
                colNavFeatured.setTypeface(null, BOLD)
                colNavGeneral.setTypeface(null, NORMAL)
                colNavWallup.setTypeface(null, NORMAL)

                colNavFeatured.setTextColor(colors.BLACK)
                colNavGeneral.setTextColor(colors.GREY_500)
                colNavWallup.setTextColor(colors.GREY_500)

                colNavFeatured.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16F)
                colNavGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colNavWallup.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colLViewPager.currentItem = 0
            }
            1 -> {
                colNavFeatured.setTypeface(null, NORMAL)
                colNavGeneral.setTypeface(null, BOLD)
                colNavWallup.setTypeface(null, NORMAL)

                colNavFeatured.setTextColor(colors.GREY_500)
                colNavGeneral.setTextColor(colors.BLACK)
                colNavWallup.setTextColor(colors.GREY_500)

                colNavFeatured.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                colNavGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)
                colNavWallup.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colLViewPager.currentItem = 1
            }
            2 -> {
                colNavFeatured.setTypeface(null, NORMAL)
                colNavGeneral.setTypeface(null, NORMAL)
                colNavWallup.setTypeface(null, BOLD)

                colNavFeatured.setTextColor(colors.GREY_500)
                colNavGeneral.setTextColor(colors.GREY_500)
                colNavWallup.setTextColor(colors.BLACK)

                colNavFeatured.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14F)
                colNavGeneral.setTextSize(TypedValue.COMPLEX_UNIT_SP,14F)
                colNavWallup.setTextSize(TypedValue.COMPLEX_UNIT_SP,16F)
                colLViewPager.currentItem = 2
            }
        }
    }
}
