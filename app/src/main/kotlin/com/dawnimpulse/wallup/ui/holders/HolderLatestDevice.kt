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
package com.dawnimpulse.wallup.ui.holders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.ui.activities.ActivityDeviceImages
import com.dawnimpulse.wallup.utils.reusables.*
import kotlinx.android.synthetic.main.holder_latest_device.view.*

class HolderLatestDevice(view: View) : RecyclerView.ViewHolder(view) {
    private val name = view.holder_latest_device_name
    private val image = view.holder_latest_device_image
    private val layout = view.holder_latest_device_layout
    private val context = view.context

    /**
     * bind data to view
     *
     * @param device
     */
    fun bind(device: ObjectDevice) {
        val point = F.displayDimensions()
        val height = 0.75 * point.y
        name.text = device.name
        layout.layoutParams.height = height.toInt()
        device.cover.imageTransform(image)
                .height(720)
                .apply()

        layout.setOnClickListener {
            context.openActivity(ActivityDeviceImages::class.java) {
                putString(DEVICE, device.toJson())
            }
        }
    }
}