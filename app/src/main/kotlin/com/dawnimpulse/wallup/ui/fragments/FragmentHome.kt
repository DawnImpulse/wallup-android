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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.ModelHome
import com.dawnimpulse.wallup.objects.ObjectUnsplashImage
import com.dawnimpulse.wallup.ui.adapters.AdapterHome
import com.dawnimpulse.wallup.utils.reusables.logd
import com.dawnimpulse.wallup.utils.reusables.toast
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * @info - main home fragment
 *
 * @author - Saksham
 * @note Last Branch Update - master
 *
 * @note Created on 2020-04-27 by Saksham
 * @note Updates :
 */
class FragmentHome : Fragment() {
    private val modelHome: ModelHome by activityViewModels()
    private lateinit var adapter: AdapterHome

    /**
     * on create view
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    /**
     * on view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelHome.getHomescreen().observe(viewLifecycleOwner, homeObserver)
    }


    /**
     * home observer
     */
    private var homeObserver = Observer<List<Any>> {
        bindRecycler(it)
    }

    /**
     *
     */
    private fun bindRecycler(list: List<Any>) {
        if (!::adapter.isInitialized) {
            adapter = AdapterHome(list)
            fragment_home_recycler.layoutManager = LinearLayoutManager(context)
            fragment_home_recycler.adapter = adapter
        } else {
            adapter.notifyDataSetChanged()
        }
    }
}