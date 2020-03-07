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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.ui.adapters.AdapterRandomImage
import com.dawnimpulse.wallup.ui.models.ModelImage
import com.dawnimpulse.wallup.utils.reusables.hide
import com.dawnimpulse.wallup.utils.reusables.show
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
class FragmentRandom : Fragment() {
    private lateinit var modelImage: ModelImage
    private lateinit var adapterRandomImage: AdapterRandomImage

    /**
     * on create view (default)
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        modelImage = ModelImage(this.activity as AppCompatActivity)
        return inflater.inflate(R.layout.fragment_random, container, false)
    }

    /**
     * on view created (default)
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelImage.getRandomQuote(30, callback)
    }

    /**
     * after images are loaded
     */
    private val callback = object : (Any?, List<ObjectImage>?) -> Unit {
        override fun invoke(error: Any?, images: List<ObjectImage>?) {

            fragment_random_anim.pauseAnimation()
            fragment_random_anim.hide()

            error?.let {
                fragment_random_error_layout.show()
            }
            images?.let {
                adapterRandomImage = AdapterRandomImage(it)
                fragment_random_recycler.layoutManager = LinearLayoutManager(context)
                fragment_random_recycler.adapter = adapterRandomImage
                fragment_random_recycler.show()
            }
        }
    }

}