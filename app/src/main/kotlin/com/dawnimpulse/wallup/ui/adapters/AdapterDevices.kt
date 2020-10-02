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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.dawnimpulse.wallup.R
import com.dawnimpulse.wallup.objects.ObjectBrand
import com.dawnimpulse.wallup.objects.ObjectDevice
import com.dawnimpulse.wallup.objects.ObjectLoading
import com.dawnimpulse.wallup.objects.ObjectReload
import com.dawnimpulse.wallup.ui.holders.HolderBrand
import com.dawnimpulse.wallup.ui.holders.HolderDevice
import com.dawnimpulse.wallup.ui.holders.HolderLoading
import com.dawnimpulse.wallup.ui.holders.HolderReload

class AdapterDevices(
        recyclerView: RecyclerView,
        private val gridLayoutManager: GridLayoutManager) : CustomAdapter<RecyclerView.ViewHolder>(recyclerView) {

    private lateinit var data: MutableList<Any>
    private val DEVICE = 0
    private val BRAND = 1
    private val LOADING = 2
    private val RELOAD = 3

    init {
        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (data[position] is ObjectDevice) 1 else 2
            }
        }
    }

    /**
     * get count
     */
    override fun getItemCount(): Int = data.size

    /**
     * get item type
     */
    override fun getItemViewType(position: Int): Int =
            when (data[position]) {
                is ObjectDevice -> DEVICE
                is ObjectBrand -> BRAND
                is ObjectLoading -> LOADING
                is ObjectReload -> RELOAD
                else -> LOADING
            }

    /**
     * create view holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            when (viewType) {
                DEVICE -> HolderDevice(LayoutInflater.from(parent.context).inflate(R.layout.holder_device, parent, false))
                BRAND -> HolderBrand(LayoutInflater.from(parent.context).inflate(R.layout.holder_brand, parent, false))
                LOADING -> HolderLoading(LayoutInflater.from(parent.context).inflate(R.layout.holder_loading, parent, false))
                RELOAD -> HolderReload(LayoutInflater.from(parent.context).inflate(R.layout.holder_reload, parent, false))
                else -> HolderDevice(LayoutInflater.from(parent.context).inflate(R.layout.holder_device, parent, false))
            }

    /**
     * bind view holder
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HolderDevice -> holder.bind(data[position] as ObjectDevice)
            is HolderBrand -> holder.bind(data[position] as ObjectBrand)
            is HolderReload -> holder.bind(data[position] as ObjectReload)
        }
    }

    /**
     * diff util callback
     */
    private class DiffUtilCallback(private val oldList: List<Any>, private val newList: List<Any>) : DiffUtil.Callback(){

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int  = newList.size

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
     * set new data
     */
    fun setData(newData: List<Any>) {
        data = newData.toMutableList()
    }

    /**
     * set new data
     */
    fun setNewData(newData: List<Any>) {
        val diffCallback = DiffUtilCallback(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }
}