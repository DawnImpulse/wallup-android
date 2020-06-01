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
package com.dawnimpulse.wallup.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.ModelImage
import com.dawnimpulse.wallup.models.ModelUnsplash
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.ui.adapters.AdapterRandomImage
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.adapter_nav_random.*
import kotlinx.android.synthetic.main.fragment_random.*

/**
 * @info - random fragment
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-03-04 by Saksham
 * @note Updates :
 */
class FragmentRandom : Fragment(R.layout.fragment_random), View.OnClickListener {
    private val modelUnsplash: ModelUnsplash by activityViewModels()
    private val modelImage: ModelImage by activityViewModels()
    private lateinit var adapterRandomImageUnsplash: AdapterRandomImage
    private lateinit var adapterRandomImage: AdapterRandomImage

    /**
     * on view created (default)
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelUnsplash.getRandomImages().observe(viewLifecycleOwner, unsplashObserver)
        modelImage.getRandomImages().observe(viewLifecycleOwner, imageObserver)
        modelUnsplash.errors().observe(viewLifecycleOwner, issueObserver)

        inflate_nav_random_wallup_card.setOnClickListener(this)
        inflate_nav_random_unsplash_card.setOnClickListener(this)
    }

    /**
     * unsplash observer
     */
    private var unsplashObserver = Observer<List<ObjectUnsplashImage?>> {
        bindRecyclerUnsplash(it)
    }

    /**
     * image observer
     */
    private var imageObserver = Observer<List<ObjectImage?>> {
        bindRecycler(it)
    }

    /**
     * issue observer
     */
    private var issueObserver = Observer<HandlerIssue> {
        fragment_random_anim.pauseAnimation()
        fragment_random_anim.hide()
        fragment_random_error_layout.show()
    }

    /**
     * after images are loaded
     * bind them to recycler
     *
     * @param images
     */
    private fun bindRecyclerUnsplash(images: List<ObjectUnsplashImage?>) {
        // case when we receive images for first time
        if (!::adapterRandomImageUnsplash.isInitialized) {
            adapterRandomImageUnsplash = AdapterRandomImage(images, fragment_random_recycler)
            adapterRandomImageUnsplash.onLoading().observe(viewLifecycleOwner, Observer {
                modelUnsplash.loadMoreRandomImages()
            })

            fragment_random_recycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            fragment_random_recycler.adapter = adapterRandomImageUnsplash
            fragment_random_recycler.show()

            fragment_random_anim.pauseAnimation()
            fragment_random_anim.hide()
        } else {
            adapterRandomImage.onLoaded()
            adapterRandomImage.notifyDataSetChanged()
        }
    }

    /**
     * after images are loaded
     * bind them to recycler
     *
     * @param images
     */
    private fun bindRecycler(images: List<ObjectImage?>) {
        // case when we receive images for first time
        if (!::adapterRandomImage.isInitialized) {
            adapterRandomImage = AdapterRandomImage(images, fragment_random_recycler_2)
            adapterRandomImage.onLoading().observe(viewLifecycleOwner, Observer {
                modelImage.loadMoreRandomImages()
            })

            fragment_random_recycler_2.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            fragment_random_recycler_2.adapter = adapterRandomImage

            fragment_random_anim.pauseAnimation()
            fragment_random_anim.hide()
        } else {
            adapterRandomImage.onLoaded()
            adapterRandomImage.notifyDataSetChanged()
        }
    }

    /**
     * on click listener handling
     */
    override fun onClick(v: View) {
        when (v.id) {
            R.id.inflate_nav_random_wallup_card -> {
                inflate_nav_random_wallup_card.setCardBackgroundColor(Colors.TEXT_PRIMARY)
                inflate_nav_random_unsplash_card.setCardBackgroundColor(Colors.PRIMARY)
                inflate_nav_random_wallup_text.setTextColor(Colors.PRIMARY)
                inflate_nav_random_unsplash_text.setTextColor(Colors.TEXT_PRIMARY)

                fragment_random_recycler.gone()
                fragment_random_recycler_2.show()
            }
            R.id.inflate_nav_random_unsplash_card -> {
                inflate_nav_random_wallup_card.setCardBackgroundColor(Colors.PRIMARY)
                inflate_nav_random_unsplash_card.setCardBackgroundColor(Colors.TEXT_PRIMARY)
                inflate_nav_random_wallup_text.setTextColor(Colors.TEXT_PRIMARY)
                inflate_nav_random_unsplash_text.setTextColor(Colors.PRIMARY)

                fragment_random_recycler.show()
                fragment_random_recycler_2.gone()
            }
        }
    }

}