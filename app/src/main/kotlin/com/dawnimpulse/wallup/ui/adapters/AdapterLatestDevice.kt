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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.objects.ObjectReload
import com.dawnimpulse.wallup.ui.holders.HolderLatestDevice
import com.dawnimpulse.wallup.ui.holders.HolderLoadingCard
import com.dawnimpulse.wallup.ui.holders.HolderReloadCard

class AdapterLatestDevice(private val viewPager2: ViewPager2) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var devices: MutableList<Any>
    private val DEVICE = 0
    private val LOADING = 1
    private val RELOAD = 2
    private var isLoading = false
    private val liveData = MutableLiveData<Void>()

    init {
        with(viewPager2){
            // page change callback
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position == devices.size - 2 && !isLoading)
                        liveData.postValue(null)
                }
            })

        }
    }

    /**
     * get item count
     */
    override fun getItemCount(): Int = devices.size

    /**
     * get item view type
     */
    override fun getItemViewType(position: Int): Int =
            when (devices[position]) {
                is ObjectDevice -> DEVICE
                is ObjectLoading -> LOADING
                is ObjectReload -> RELOAD
                else -> LOADING
            }

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                DEVICE -> HolderLatestDevice(LayoutInflater.from(parent.context).inflate(R.layout.holder_latest_device, parent, false))
                LOADING -> HolderLoadingCard(LayoutInflater.from(parent.context).inflate(R.layout.holder_loading_card, parent, false))
                RELOAD -> HolderReloadCard(LayoutInflater.from(parent.context).inflate(R.layout.holder_reload_card, parent, false))
                else -> HolderLatestDevice(LayoutInflater.from(parent.context).inflate(R.layout.holder_latest_device, parent, false))
            }

    /**
     * bind view holder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HolderLatestDevice -> holder.bind(devices[position] as ObjectDevice)
            is HolderLoadingCard -> holder.bind()
            is HolderReloadCard -> holder.bind(devices[position] as ObjectReload)
        }
    }

    /**
     * when its time to load new data
     *
     * @return LiveData<Void>
     */
    fun onLoading(): LiveData<Void> {
        return liveData
    }

    /**
     * data is loaded , set variable false
     */
    fun onLoaded() {
        isLoading = false
    }

    /**
     * diff util callback
     */
    private class DiffUtilCallback(private val oldList: List<Any>, private val newList: List<Any>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return true
        }
    }

    /**
     * set data
     *
     * @param devices
     */
    fun setData(devices: List<Any>) {
        this.devices = devices.toMutableList()
    }

    /**
     * set new data
     */
    fun setNewData(newData: List<Any>) {
        val diffCallback = DiffUtilCallback(devices, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        devices.clear()
        devices.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}