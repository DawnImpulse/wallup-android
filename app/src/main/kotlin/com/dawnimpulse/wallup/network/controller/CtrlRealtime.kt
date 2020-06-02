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
package com.dawnimpulse.wallup.network.controller

import com.dawnimpulse.wallup.utils.reusables.COUNTER
import com.dawnimpulse.wallup.utils.reusables.IMAGES
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object CtrlRealtime {
    private val realtime = Firebase.database.reference

    /**
     * increment download/wallpaper/likes etc counter
     */
    fun incrementCounter(id: String, type:String) {
        realtime
                .child(COUNTER)
                .child(IMAGES)
                .child(id)
                .child(type)
                .runTransaction(object : Transaction.Handler {

                    // do transaction
                    override fun doTransaction(data: MutableData): Transaction.Result {
                        var count = data.getValue(Int::class.java)
                        if (count == null) count = 1 else count++
                        data.value = count
                        return Transaction.success(data)
                    }

                    // on transaction completed
                    override fun onComplete(e: DatabaseError?, p1: Boolean, data: DataSnapshot?) {
                        //
                    }

                })
    }

    /**
     * decrease download/wallpaper/likes etc counter
     */
    fun decreaseCounter(id: String, type:String) {
        realtime
                .child(COUNTER)
                .child(IMAGES)
                .child(id)
                .child(type)
                .runTransaction(object : Transaction.Handler {

                    // do transaction
                    override fun doTransaction(data: MutableData): Transaction.Result {
                        var count = data.getValue(Int::class.java)
                        if (count == null) count = 0 else count--
                        data.value = count
                        return Transaction.success(data)
                    }

                    // on transaction completed
                    override fun onComplete(e: DatabaseError?, p1: Boolean, data: DataSnapshot?) {
                        //
                    }

                })
    }
}