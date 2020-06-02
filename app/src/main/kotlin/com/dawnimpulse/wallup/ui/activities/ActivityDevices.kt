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

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.ModelDevices
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.ui.adapters.AdapterDevices
import com.dawnimpulse.wallup.utils.reusables.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.layout_general.*

class ActivityDevices : AppCompatActivity(R.layout.layout_general) {
    private val modelHome: ModelDevices by viewModels()
    private lateinit var adapter: AdapterDevices
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelHome.getList().observe(this, deviceObserver)
        modelHome.errors().observe(this, errorObserver)
        disposable.add(RxBusType.subscribe { rxType(it) })

        layout_general_loading.playAnimation()

        // handling reload click
        layout_general_error_reload.setOnClickListener {
            layout_general_error_anim.pauseAnimation()
            layout_general_error_layout.gone()
            layout_general_loading.show()
            layout_general_loading.playAnimation()
            modelHome.reload()
        }
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
        if (type.type == RELOAD_LIST && type.data == RELOAD.MORE.A_DEVICES)
            modelHome.loadMore()
    }

    /**
     * home observer
     */
    private var deviceObserver = Observer<List<Any>> {
        bindRecycler(it)
    }

    /**
     * errors observer
     */
    private val errorObserver = Observer<ObjectIssue> {
        layout_general_loading.pauseAnimation()
        layout_general_loading.gone()
        layout_general_error_layout.show()
        layout_general_error_anim.playAnimation()
    }

    /**
     * load more observer
     */
    private val loadMoreObserver = Observer<Void> {
        modelHome.loadMore()
    }

    /**
     * bind recycler
     */
    private fun bindRecycler(list: List<Any>) {
        if (!::adapter.isInitialized) {
            val layoutManager = GridLayoutManager(this, 2)
            adapter = AdapterDevices(layout_general_recycler, layoutManager)
            adapter.setData(list)
            layout_general_recycler.layoutManager = layoutManager
            layout_general_recycler.adapter = adapter
            adapter.onLoading().observe(this, loadMoreObserver)

            layout_general_recycler.show()
            layout_general_loading.pauseAnimation()
            layout_general_loading.gone()
        } else
            adapter.setNewData(list)
        adapter.onLoaded()
    }
}