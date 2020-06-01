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
package com.dawnimpulse.wallup.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.ui.holders.HolderLatestDevice

class AdapterLatestDevice(recyclerView: RecyclerView) : CustomAdapter<RecyclerView.ViewHolder>(recyclerView) {

    private lateinit var devices: MutableList<Any>
    private val DEVICE = 0
    private val LOADING = 1

    /**
     * get item count
     */
    override fun getItemCount(): Int = devices.size

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                DEVICE -> HolderLatestDevice(LayoutInflater.from(parent.context).inflate(R.layout.holder_latest_device, parent, false))
                else -> HolderLatestDevice(LayoutInflater.from(parent.context).inflate(R.layout.holder_latest_device, parent, false))
            }

    /**
     * bind view holder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HolderLatestDevice -> holder.bind(devices[position] as ObjectDevice)
        }
    }

    /**
     * set data
     *
     * @param devices
     */
    fun setData(devices: List<Any>){
        this.devices = devices.toMutableList()
    }
}