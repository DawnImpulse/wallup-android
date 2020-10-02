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
import com.dawnimpulse.wallup.utils.reusables.DEVICE
import com.dawnimpulse.wallup.utils.reusables.imageTransform
import com.dawnimpulse.wallup.utils.reusables.openActivity
import com.dawnimpulse.wallup.utils.reusables.toJson
import kotlinx.android.synthetic.main.holder_device.view.*

class HolderDevice(view: View) : RecyclerView.ViewHolder(view) {
    private val name = view.holder_device_name
    private val image = view.holder_device_bg
    private val context = view.context

    /**
     * bind data to view
     *
     * @param device
     */
    fun bind(device: ObjectDevice) {
        name.text = device.name
        device.cover.imageTransform(image)
                .height(360)
                .apply()

        image.setOnClickListener {
            context.openActivity(ActivityDeviceImages::class.java) {
                putString(DEVICE, device.toJson())
            }
        }
    }
}