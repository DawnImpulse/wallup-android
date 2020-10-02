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
import androidx.recyclerview.widget.LinearLayoutManager
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.models.ModelBookmarks
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.objects.ObjectImage
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.ui.adapters.AdapterImage
import com.dawnimpulse.wallup.utils.reusables.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.layout_general.*

class ActivityBookmarks : AppCompatActivity(R.layout.layout_general) {
    private val modelBookmarks: ModelBookmarks by viewModels()
    private lateinit var adapter: AdapterImage
    private lateinit var device: ObjectDevice
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        modelBookmarks.getImages().observe(this, deviceObserver)
        modelBookmarks.errors().observe(this, errorObserver)
        disposable.add(RxBusType.subscribe { rxType(it) })

        layout_general_loading.playAnimation()

        // handling reload click
        layout_general_error_reload.setOnClickListener {
            layout_general_error_anim.pauseAnimation()
            layout_general_error_layout.gone()
            layout_general_loading.show()
            layout_general_loading.playAnimation()
            modelBookmarks.reload()
        }
    }

    /**
     * on resume
     */
    override fun onResume() {
        super.onResume()

        when {
            AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES -> window.decorView.systemUiVisibility = 0
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M -> {
                window.statusBarColor = Colors.BLACK
            }
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
        when (type.type) {
            RELOAD_LIST -> if (type.data == RELOAD.MORE.BOOKMARKS) modelBookmarks.loadMore()
            EVENT.REMOVE.BOOKMARK -> modelBookmarks.remove(type.data as String)
            EVENT.ADD.BOOKMARK -> modelBookmarks.add(type.data as ObjectImage)
            EVENT.NOT.BOOKMARKS -> {
                layout_general_not_found_layout.show()
                layout_general_not_found_anim.playAnimation()
            }
        }

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
        modelBookmarks.loadMore()
    }

    /**
     * bind recycler
     */
    private fun bindRecycler(list: List<Any>) {
        if (!::adapter.isInitialized) {
            adapter = AdapterImage(layout_general_recycler)
            adapter.setData(list)
            layout_general_recycler.layoutManager = LinearLayoutManager(this)
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