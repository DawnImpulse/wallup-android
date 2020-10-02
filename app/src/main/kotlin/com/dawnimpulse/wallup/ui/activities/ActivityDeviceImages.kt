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
import com.dawnimpulse.wallup.models.ModelDeviceImages
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.objects.ObjectIssue
import com.dawnimpulse.wallup.ui.adapters.AdapterImage
import com.dawnimpulse.wallup.utils.reusables.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_device_images.*

class ActivityDeviceImages : AppCompatActivity(R.layout.activity_device_images) {
    private val modelDeviceImages: ModelDeviceImages by viewModels()
    private lateinit var adapter: AdapterImage
    private lateinit var device: ObjectDevice
    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        device = intent.extras!!.getString(DEVICE,"").fromSafeJson(ObjectDevice::class.java)!!
        modelDeviceImages.getList(device._id).observe(this, deviceObserver)
        modelDeviceImages.errors().observe(this, errorObserver)
        disposable.add(RxBusType.subscribe { rxType(it) })

        activity_device_images_loading.playAnimation()

        // device appbar image & name
        activity_device_images_name.text = device.name
        device.cover.imageTransform(activity_device_images_bg)
                .height(480)
                .apply()

        // handling reload click
        activity_device_images_error_reload.setOnClickListener {
            activity_device_images_error_anim.pauseAnimation()
            activity_device_images_error_layout.gone()
            activity_device_images_loading.show()
            activity_device_images_loading.playAnimation()
            modelDeviceImages.reload()
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
        if (type.type == RELOAD_LIST && type.data == RELOAD.MORE.D_IMAGES)
            modelDeviceImages.loadMore()
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
        activity_device_images_loading.pauseAnimation()
        activity_device_images_loading.gone()
        activity_device_images_error_layout.show()
        activity_device_images_error_anim.playAnimation()
    }

    /**
     * load more observer
     */
    private val loadMoreObserver = Observer<Void> {
        modelDeviceImages.loadMore()
    }

    /**
     * bind recycler
     */
    private fun bindRecycler(list: List<Any>) {
        if (!::adapter.isInitialized) {
            adapter = AdapterImage(activity_device_images_recycler)
            adapter.setData(list)
            activity_device_images_recycler.layoutManager = LinearLayoutManager(this)
            activity_device_images_recycler.adapter = adapter
            adapter.onLoading().observe(this, loadMoreObserver)

            activity_device_images_recycler.show()
            activity_device_images_loading.pauseAnimation()
            activity_device_images_loading.gone()
        } else
            adapter.setNewData(list)
        adapter.onLoaded()
    }
}