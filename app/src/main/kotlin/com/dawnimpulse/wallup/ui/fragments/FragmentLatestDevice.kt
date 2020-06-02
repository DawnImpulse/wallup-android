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
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.ModelLatestDevice
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.ui.adapters.AdapterLatestDevice
import com.dawnimpulse.wallup.utils.reusables.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_devices_latest.*

class FragmentLatestDevice : Fragment(R.layout.fragment_devices_latest) {
    private val modelLatestDevice: ModelLatestDevice by activityViewModels()
    private lateinit var adapter: AdapterLatestDevice
    private var disposable = CompositeDisposable()

    /**
     * on view created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelLatestDevice.getList().observe(viewLifecycleOwner, deviceObserver)
        modelLatestDevice.errors().observe(viewLifecycleOwner, errorObserver)
        disposable.add(RxBusType.subscribe { rxType(it) })

        fragment_devices_latest_loading.playAnimation()

        // handling reload click
        fragment_devices_latest_error_reload.setOnClickListener {
            fragment_devices_latest_error_anim.pauseAnimation()
            fragment_devices_latest_error_layout.gone()
            fragment_devices_latest_loading.show()
            fragment_devices_latest_loading.playAnimation()
            modelLatestDevice.reload()
        }
    }

    /**
     * on destroy
     */
    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    /**
     * handle rx type
     */
    private fun rxType(type: RxType) {
        toast("here")
        if (type.type == RELOAD_LIST && type.data == RELOAD.MORE.L_DEVICES)
            modelLatestDevice.loadMore()
    }

    /**
     * home observer
     */
    private var deviceObserver = Observer<List<Any>> {
        bindViewpager(it)
    }

    /**
     * errors observer
     */
    private val errorObserver = Observer<ObjectIssue> {
        fragment_devices_latest_loading.pauseAnimation()
        fragment_devices_latest_loading.gone()
        fragment_devices_latest_error_layout.show()
        fragment_devices_latest_error_anim.playAnimation()
    }

    /**
     * load more observer
     */
    private val loadMoreObserver = Observer<Void> {
        modelLatestDevice.loadMore()
    }

    /**
     * set viewpager
     */
    private fun setViewpager() {
        with(fragment_devices_latest_viewpager) {
            // page limit
            offscreenPageLimit = 2
            // page transformer
            setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * 24.dpToPx() + 16.dpToPx())
                if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.translationX = -offset
                    } else {
                        page.translationX = offset
                    }
                } else {
                    page.translationY = offset
                }
            }
        }
    }

    /**
     * bind viewpager
     */
    private fun bindViewpager(list: List<Any>) {
        if (!::adapter.isInitialized) {
            adapter = AdapterLatestDevice(fragment_devices_latest_viewpager)
            adapter.setData(list)
            setViewpager()
            fragment_devices_latest_viewpager.adapter = adapter
            adapter.onLoading().observe(viewLifecycleOwner, loadMoreObserver)

            fragment_devices_latest_viewpager.show()
            fragment_devices_latest_loading.pauseAnimation()
            fragment_devices_latest_loading.gone()
        } else
            adapter.setNewData(list)
        adapter.onLoaded()
    }
}